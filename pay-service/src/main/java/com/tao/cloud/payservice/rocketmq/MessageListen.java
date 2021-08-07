package com.tao.cloud.payservice.rocketmq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListen implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(MessageListen.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                    ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        for (MessageExt messageExt : list) {
            String message = new String(messageExt.getBody());
            logger.info("[MessageListen][consumeMessage]: {}", message);
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
