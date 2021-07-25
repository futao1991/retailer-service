package com.tao.cloud.payservice.rocketmq;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class RocketMQProducer {

    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.producer.instanceName}")
    private String instanceName;

    @Value("${rocketmq.producer.maxMessageSize}")
    private int maxMessageSize;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));

    @Autowired
    private OrderTransactionListener transactionListener;

    @Bean
    public TransactionMQProducer getRocketMQProducer() throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer();
        producer.setProducerGroup(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName(instanceName);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setExecutorService(executor);
        producer.setTransactionListener(transactionListener);
        producer.start();

        return producer;
    }
}
