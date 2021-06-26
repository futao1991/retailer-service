package com.tao.cloud.service;

import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.model.Commodity;
import com.tao.cloud.response.DeductWarehouseResponse;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;


@LocalTCC
public interface WarehouseService {

    /**
     * 查询某件商品的库存
     * @param commodityId  商品id
     * @return 商品库存 商品不存在时返回-1
     */
    Commodity queryWarehouseCount(String commodityId);

    /**
     * 扣减库存操作tcc
     * @param commodityId 商品id
     * @param count       待扣减的库存量
     * @return            操作结果
     * @throws BusinessException 异常
     */
    @TwoPhaseBusinessAction(name = "deductWarehouse", commitMethod = "commitDeduct", rollbackMethod = "cancelDeduct")
    DeductWarehouseResponse deductWarehouse(
            @BusinessActionContextParameter(paramName = "commodityId") String commodityId,
            @BusinessActionContextParameter(paramName = "orderId") String orderId,
            @BusinessActionContextParameter(paramName = "count") Integer count) throws BusinessException;

    /**
     * 确认扣减库存
     * @param context 分布式事物context
     * @return true/false
     */
    boolean commitDeduct(BusinessActionContext context);

    /**
     * 取消扣减库存
     * @param context 分布式事物context
     * @return true/false
     */
    boolean cancelDeduct(BusinessActionContext context);

    /**
     * 恢复库存
     * @param commodityId 商品id
     * @param orderId     订单id
     * @return            操作结果
     */
    DeductWarehouseResponse restoreWarehouse(String commodityId, String orderId) throws BusinessException;
}
