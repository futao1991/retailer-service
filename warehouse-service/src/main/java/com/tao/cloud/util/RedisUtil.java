package com.tao.cloud.util;

import com.tao.cloud.config.ErrorType;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class RedisUtil implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("console");

    @Autowired
    private RedisTemplate redisTemplate;

    private String deductRecordLuaScript = "";

    private String restoreRecordLuaScript = "";

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
        deductRecordLuaScript = getLuaScript("deduct_warehouse.lua");
        restoreRecordLuaScript = getLuaScript("restore_warehouse.lua");
        logger.info("加载lua脚本完毕");
    }

    /**
     * 通过redis脚本扣减库存
     * @param commodityId  商品id
     * @param count        待扣减的库存
     * @param orderId      待生成的订单id
     * @return             <状态码, 错误信息>
     */
    public Pair<String, String> deductWarehouse(String commodityId, Integer count, String orderId) {
        try {
            DefaultRedisScript<String> script = new DefaultRedisScript<>();
            script.setResultType(String.class);
            script.setScriptText(deductRecordLuaScript);
            String result = (String)redisTemplate.execute(
                    script, Arrays.asList(commodityId, String.valueOf(count), orderId), new ArrayList<>());
            return Pair.with(result, null);
        } catch (Exception e) {
            logger.error("execute lua script failed: ", e);
            return Pair.with(ErrorType.ERROR_INTERNAL.getErrorCode(), e.getMessage());
        }
    }

    /**
     * 通过redis脚本恢复库存
     * @param commodityId  商品id
     * @param orderId      订单id
     * @return             <状态码, 错误信息>
     */
    public Pair<String, String> restoreWarehouse(String commodityId, String orderId) {
        try {
            DefaultRedisScript<String> script = new DefaultRedisScript<>();
            script.setResultType(String.class);
            script.setScriptText(restoreRecordLuaScript);
            String result = (String)redisTemplate.execute(script, Arrays.asList(commodityId, orderId), new ArrayList<>());
            return Pair.with(result, null);
        } catch (Exception e) {
            logger.error("execute lua script failed: ", e);
            return Pair.with(ErrorType.ERROR_INTERNAL.getErrorCode(), e.getMessage());
        }
    }
}
