package com.tao.cloud.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConsumer.class);

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.topic}")
    private String topic;

    @Value("${rocketmq.consumer.tag}")
    private String tag;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;

    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Autowired
    private MessageListen messageListen;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer getConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setVipChannelEnabled(false);
        consumer.setMessageListener(messageListen);

        try {
            consumer.subscribe(topic, tag);
            logger.info("[RocketMQConsumer][getConsumer]: start monitor topic {}", topic);
        } catch (Exception e) {
            logger.error("[RocketMQConsumer][getConsumer]: getConsumer error:", e);
        }

        return consumer;
    }
}
