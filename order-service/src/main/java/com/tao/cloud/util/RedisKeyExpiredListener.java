package com.tao.cloud.util;

import com.tao.cloud.feign.WarehouseService;
import org.apache.commons.lang3.StringUtils;
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

        String commodityId = RedisUtil.getCommodityIdFromRedisKey(expiredKey);
        String orderId = RedisUtil.getOrderIdFromRedisKey(expiredKey);
        if (StringUtils.isNotEmpty(commodityId)) {
            logger.info("订单{}超时未支付, 准备恢复商品{}的库存", expiredKey, commodityId);
            warehouseService.restoreWarehouse(commodityId, orderId);
        }
    }
}
