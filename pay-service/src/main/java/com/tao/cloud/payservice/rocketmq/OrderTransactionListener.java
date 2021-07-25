package com.tao.cloud.payservice.rocketmq;


import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.model.MQTransaction;
import com.tao.cloud.payservice.mapper.MQTransactionMapper;
import com.tao.cloud.payservice.service.UserService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderTransactionListener implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderTransactionListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MQTransactionMapper transactionMapper;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {

        logger.info("开始执行本地事务");
        String body = new String(message.getBody());
        OrderMessage orderMessage = OrderMessage.toOrderMessage(body);
        if (null == orderMessage) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        Long userId = orderMessage.getUserId();
        Long totalMoney = orderMessage.getTotalPrice();
        String orderId = orderMessage.getOrderId();
        String transactionId = message.getTransactionId();

        try {
            if (userService.addOrDecreaseMoney(userId, totalMoney, transactionId, orderId)) {
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        } catch (Exception e) {
            logger.error("执行本地事务失败:", e);
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

        String transactionId = messageExt.getTransactionId();
        MQTransaction transaction = transactionMapper.getTransaction(transactionId);
        if (null != transaction) {
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        return LocalTransactionState.UNKNOW;
    }
}
