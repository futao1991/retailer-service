package com.tao.cloud.rocketmq;

import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.mapper.OrderMapper;
import com.tao.cloud.model.OrderInfo;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class MessageListen implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(MessageListen.class);

    private final String ORDER_ADD = "add";

    private final String ORDER_UPDATE = "update";

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获取订单对应的操作类型
     * @param orderMessage  订单消息
     * @return add或者update
     */
    private String getOrderOperation(OrderMessage orderMessage) {
        if (orderMessage.getStatus() == OrderMessage.OrderStatus.WAITDELIVERY) {
            return ORDER_ADD;
        }
        return ORDER_UPDATE;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        for (MessageExt messageExt : list) {
            String value = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            OrderMessage orderMessage = OrderMessage.toOrderMessage(value);
            if (null == orderMessage) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            logger.info("[MessageListen][consumeMessage]: add or update order {}", orderMessage.getOrderId());

            String operation = getOrderOperation(orderMessage);
            OrderInfo orderInfo = orderMessage.toOrderInfo();
            try {
                if (ORDER_ADD.equals(operation)) {
                    orderMapper.addOrder(orderInfo);
                } else {
                    orderMapper.updateOrder(orderInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("[MessageListen][consumeMessage]: add or update order error {}", e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
