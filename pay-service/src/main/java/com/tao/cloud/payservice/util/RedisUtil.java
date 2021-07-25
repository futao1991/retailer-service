package com.tao.cloud.payservice.util;

import com.tao.cloud.config.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    private static final String orderPrefix = "retailerOrder";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从redis缓存中获取订单信息
     * @param orderId 订单id
     * @return
     */
    public OrderMessage getOrderMessageById(String orderId) {
        Object oject = redisTemplate.opsForValue().get(String.format("%s-%s", orderPrefix, orderId));
        return null != oject ? OrderMessage.toOrderMessage(oject.toString()) : null;
    }
}
