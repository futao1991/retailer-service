package com.tao.cloud.controller;

import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.response.DeductWarehouseResponse;
import com.tao.cloud.response.GetWarehouseResponse;
import com.tao.cloud.response.OrderResponse;
import com.tao.cloud.service.OrderService;
import com.tao.cloud.service.WarehouseService;
import com.tao.cloud.util.OrderUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private OrderService orderService;

    /**
     * 查询某件商品的库存
     * @param commodityId  商品id
     * @return  商品库存
     */
    @GetMapping("/query")
    @ResponseBody
    public GetWarehouseResponse getGetWarehouseResponse(String commodityId) {
        return warehouseService.queryWarehouseCount(commodityId);
    }

    @RequestMapping("/deduct")
    @Transactional
    @GlobalTransactional(timeoutMills = 60000 * 2)
    public DeductWarehouseResponse deductWareHouse(@RequestParam("commodityId") String commodityId,
                                                   @RequestParam("orderId") String orderId,
                                                   @RequestParam("count") Integer count) {
        return warehouseService.deductWareHouse(commodityId, orderId, count);
    }

    /**
     * 用户下单接口
     * @param commodityId   商品id
     * @param count         购买数量
     * @return              订单结果
     * @throws BusinessException
     */
    @RequestMapping("/placeOrder")
    @Transactional
    @GlobalTransactional(timeoutMills = 60000 * 2)
    @ResponseBody
    public OrderResponse placeOrder(@RequestParam("commodityId") String commodityId,
                                    @RequestParam("count") Integer count) throws BusinessException {
        String orderId = OrderUtil.createOrderId();
        DeductWarehouseResponse deduct = warehouseService.deductWareHouse(commodityId, orderId, count);
        if (null != deduct.getErrMsg()) {
            throw new BusinessException(deduct.getErrMsg());
        }

        return orderService.createOrder(commodityId, orderId, count);
    }
}
