package com.tao.cloud.util;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String orderPrefix = "retailerOrder";

    private String createOrderLuaScript = "";

    @Value("${order.key.timeout}")
    private Integer orderTimeOut;

    @Value("${order.key.temp-order-count}")
    private String tempRedisCount;

    private String getLuaScript(String fileName) {
        try (InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            return script.toString();
        } catch (Exception e) {
            logger.error("get lua script error", e);
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createOrderLuaScript = getLuaScript("create_order.lua");
        logger.info("加载lua脚本完毕");
    }

    /**
     * 生成redis的key
     * @param orderId      订单id
     * @param commodityId  商品id
     * @return             rediskey
     */
    private String getRedisKeyFromOrderId(String orderId, String commodityId) {
        return String.format("%s-%s-%s", orderPrefix, orderId, commodityId);
    }

    public static String getOrderIdFromRedisKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return StringUtils.substringBetween(key, "-");
    }

    public static Boolean isRedisKeyForOrderId(String key) {
        return StringUtils.startsWith(key, orderPrefix);
    }

    public static String getCommodityIdFromRedisKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return StringUtils.substringAfterLast(key, "-");
    }

    public void insertOrder(String orderId, OrderMessage orderMessage) {
        redisTemplate.opsForValue().set(getRedisKeyFromOrderId(orderId, orderMessage.getCommodityId()),
                orderMessage.toJsonString(), orderTimeOut, TimeUnit.SECONDS);
    }

    public boolean deleteOrder(String orderId, String commodityId) {
        return redisTemplate.delete(getRedisKeyFromOrderId(orderId, commodityId));
    }
}
