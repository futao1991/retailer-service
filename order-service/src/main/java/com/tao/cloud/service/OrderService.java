package com.tao.cloud.service;

import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.exception.BusinessException;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface OrderService {

    /**
     *
     * @param commodityId
     * @return
     */
    @TwoPhaseBusinessAction(name = "createOrder", commitMethod = "commitOrder", rollbackMethod = "cancelOrder")
    OrderMessage createOrder(@BusinessActionContextParameter(paramName = "orderId") String orderId,
                             @BusinessActionContextParameter(paramName = "userId") Long userId,
                             @BusinessActionContextParameter(paramName = "commodityId") String commodityId,
                             @BusinessActionContextParameter(paramName = "count") Integer count);

    /**
     * 确认创建订单
     * @param context 分布式事物context
     * @return true/false
     */
    boolean commitOrder(BusinessActionContext context);

    /**
     * 取消创建订单
     * @param context 分布式事物context
     * @return true/false
     */
    boolean cancelOrder(BusinessActionContext context);


    /**
     * 删除订单
     * @param orderId  订单id
     * @return
     */
    OrderMessage deleteOrder(String orderId);
}
