package com.tao.cloud.payservice.service;

import com.alibaba.fastjson.JSON;
import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.model.User;
import com.tao.cloud.payservice.mapper.UserMapper;
import com.tao.cloud.payservice.util.RedisUtil;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class MQService {

    private static final Logger logger = LoggerFactory.getLogger(MQService.class);

    @Value("${rocketmq.producer.topic}")
    private String topic;

    @Value("${rocketmq.producer.tag}")
    private String tag;

    @Value("${rocketmq.producer.sendMsgTimeout}")
    private long sendMsgTimeout;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TransactionMQProducer producer;

    /**
     * 发送订单消息到mq中
     * @param orderId 订单id
     * @throws BusinessException
     */
    public OrderMessage payOrder(String orderId, Long userId) throws BusinessException {
        OrderMessage orderMessage = redisUtil.getOrderMessageById(orderId);
        if (null == orderMessage) {
            throw new BusinessException(ErrorType.ERROR_ORDER_NOTEXIST);
        }

        if (!Objects.equals(orderMessage.getUserId(), userId)) {
            throw new BusinessException(ErrorType.ERROR_USER_NOTOWNER);
        }
        User user = userMapper.getUserById(userId);
        if (null == user) {
            throw new BusinessException(ErrorType.ERROR_USER_NOTEXIST);
        }

        if (user.getMoney() < orderMessage.getTotalPrice()) {
            throw new BusinessException(ErrorType.ERROR_MONEY_NOTENOUGH);
        }

        orderMessage.setStatus(OrderMessage.OrderStatus.WAITDELIVERY);
        Message message = new Message(topic, tag, JSON.toJSONString(orderMessage).getBytes(StandardCharsets.UTF_8));
        try {
            SendResult sendResult =  producer.sendMessageInTransaction(message, null);
            logger.info("[MQService][payOrder]: {}", sendResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorType.ERROR_SEND_MQ.getErrorCode(), e.getMessage());
        }
        return orderMessage;
    }
}
