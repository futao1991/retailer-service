package com.tao.cloud.util;

import com.tao.cloud.config.OrderMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    @Value("${order.key.price-table}")
    private String priceTable;

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
     * @return             rediskey
     */
    private String getRedisKeyFromOrderId(String orderId) {
        return String.format("%s-%s", orderPrefix, orderId);
    }

    /**
     * 从redis key中获取订单id
     * @param key
     * @return
     */
    public static String getOrderIdFromRedisKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return StringUtils.substringAfter(key, orderPrefix);
    }

    public static Boolean isRedisKeyForOrderId(String key) {
        return StringUtils.startsWith(key, orderPrefix);
    }

    /**
     * 将订单信息插入到redis缓存中
     * @param orderId       订单id
     * @param orderMessage  订单信息
     */
    public void insertOrder(String orderId, OrderMessage orderMessage) {
        redisTemplate.opsForValue().set(getRedisKeyFromOrderId(orderId),
                orderMessage.toJsonString(), orderTimeOut, TimeUnit.SECONDS);
    }

    /**
     * 删除redis中的订单信息
     * @param orderId      订单id
     * @return
     */
    public boolean deleteOrder(String orderId) {
        return redisTemplate.delete(getRedisKeyFromOrderId(orderId));
    }

    /**
     * 从redis缓存中获取订单信息
     * @param orderId 订单id
     * @return
     */
    public OrderMessage getOrderMessageByKey(String orderId) {
        Object oject = redisTemplate.opsForValue().get(getRedisKeyFromOrderId(orderId));
        return null != oject ? OrderMessage.toOrderMessage(oject.toString()) : null;
    }

    /**
     * 从redis缓存中获取商品价格
     * @param commodityId 商品id
     * @return
     */
    public long getCommdityPrice(String commodityId) {
        Object oject = redisTemplate.opsForHash().get(priceTable, commodityId);
        return null != oject ? Long.valueOf(oject.toString()) : -1;
    }
}
