package com.tao.cloud.controller;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.model.Commodity;
import com.tao.cloud.response.DeductWarehouseResponse;
import com.tao.cloud.response.GetWarehouseResponse;
import com.tao.cloud.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 查询某件商品的库存
     * @param commodityId  商品id
     * @return  商品库存
     */
    @GetMapping("/query")
    @ResponseBody
    public GetWarehouseResponse getWarehouseCount(@RequestParam("commodityId") String commodityId) {
        Commodity commodity = warehouseService.queryWarehouseCount(commodityId);
        if (null == commodity) {
            return GetWarehouseResponse.createResponse(null, ErrorType.ERROR_WAREHOUSE_NOTEXIST);
        }
        return GetWarehouseResponse.createResponse(commodity, null);
    }

    /**
     * 扣减某件商品的库存
     * @param commodityId  商品id
     * @param count        待扣减的数量
     * @return             操作结果
     */
    @RequestMapping("/deduct")
    @ResponseBody
    public DeductWarehouseResponse deductWarehouse(@RequestParam("commodityId") String commodityId,
                                                   @RequestParam("orderId") String orderId,
                                                   @RequestParam("count") Integer count) {
        return warehouseService.deductWarehouse(commodityId, orderId, count);
    }

    /**
     * 恢复某件商品的库存
     * @param commodityId  商品id
     * @return             操作结果
     */
    @RequestMapping("/restore")
    @ResponseBody
    public DeductWarehouseResponse restoreWarehouse(@RequestParam("commodityId") String commodityId,
                                                    @RequestParam("orderId") String orderId) {
        System.out.println(String.format("commodityId: %s, orderId: %s", commodityId, orderId));
        return warehouseService.restoreWarehouse(commodityId, orderId);
    }
}
