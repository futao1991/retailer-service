package com.tao.cloud.service.impl;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.response.DeductWarehouseResponse;
import com.tao.cloud.service.WarehouseService;
import com.tao.cloud.util.OrderUtil;
import com.tao.cloud.util.RedisUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private static final String OK_MSG = "OK";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * redis存储的库存表名
     */
    @Value("${warehouse.redis-table-name}")
    private String warehouseName;

    /**
     * redis存储的库存表名
     */
    @Value("${warehouse.frozen-table-name}")
    private String frozenTableName;

    @Override
    public int queryWarehouseCount(String commodityId) {
        Object value = redisTemplate.opsForHash().get(warehouseName, commodityId);
        return null != value ? Integer.valueOf(value.toString()) : -1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public DeductWarehouseResponse deductWarehouse(String commodityId, String orderId, Integer count)
            throws BusinessException {
        Pair<String, String> result = redisUtil.deductWarehouse(commodityId, count, orderId);
        if (OK_MSG.equals(result.getValue0())) {
            return new DeductWarehouseResponse(commodityId, orderId, OK_MSG);
        } else if (ErrorType.ERROR_WAREHOUSE_NOTEXIST.getErrorCode().equals(result.getValue0())) {
            return new DeductWarehouseResponse(commodityId, ErrorType.ERROR_WAREHOUSE_NOTEXIST);
        } else if (ErrorType.ERROR_NOT_ENOUGH_WAREHOUSE.getErrorCode().equals(result.getValue0())) {
            return new DeductWarehouseResponse(commodityId, ErrorType.ERROR_NOT_ENOUGH_WAREHOUSE);
        } else {
            throw new BusinessException(result.getValue0(), result.getValue1());
        }
    }

    @Override
    public boolean commitDeduct(BusinessActionContext context) {
        logger.info("事务{}提交成功", context.getXid());
        return true;
    }

    @Override
    public boolean cancelDeduct(BusinessActionContext context) {
        logger.info("准备回滚事务{}", context.getXid());
        String commodityId = (String)context.getActionContext("commodityId");
        String orderId = (String)context.getActionContext("orderId");
        Pair<String, String> result = redisUtil.restoreWarehouse(commodityId, orderId);
        logger.info("回滚事务{}完毕", context.getXid());
        return true;
    }

    @Override
    public DeductWarehouseResponse restoreWarehouse(String commodityId, String orderId) throws BusinessException {
        Pair<String, String> result = redisUtil.restoreWarehouse(commodityId, orderId);
        if (OK_MSG.equals(result.getValue0())) {
            return new DeductWarehouseResponse(commodityId, orderId, OK_MSG);
        } else if (ErrorType.ERROR_WAREHOUSE_NOTEXIST.getErrorCode().equals(result.getValue0())) {
            return new DeductWarehouseResponse(commodityId, ErrorType.ERROR_WAREHOUSE_NOTEXIST);
        } else {
            throw new BusinessException(result.getValue0(), result.getValue1());
        }
    }
}
