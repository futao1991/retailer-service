package com.tao.cloud.util;

import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.feign.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyExpiredListener.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WarehouseService warehouseService;

    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (!RedisUtil.isRedisKeyForOrderId(expiredKey)) {
            return;
        }

        if (!RedisUtil.isRedisKeyForOrderId(expiredKey)) {
            return;
        }

        String orderId = RedisUtil.getOrderIdFromRedisKey(expiredKey);
        OrderMessage orderMessage = redisUtil.getOrderMessageByKey(orderId);
        if (null != orderMessage) {
            String commodityId = orderMessage.getCommodityId();
            logger.info("订单{}超时未支付, 准备恢复商品{}的库存", orderId, commodityId);
            warehouseService.restoreWarehouse(commodityId, orderId);
        }
    }
}
