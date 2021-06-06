package com.tao.cloud.service;

import com.tao.cloud.response.DeductWarehouseResponse;
import com.tao.cloud.response.GetWarehouseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "warehouse-service")
public interface WarehouseService {

    /**
     * 查询某件商品的库存
     * @param commodityId  商品id
     * @return 商品库存 商品不存在时返回-1
     */
    @GetMapping("/warehouse/query")
    GetWarehouseResponse queryWarehouseCount(@RequestParam("commodityId") String commodityId);

    /**
     * 扣减某件商品的库存
     * @param commodityId  商品id
     * @param count        待扣减的数量
     * @return             本次扣减结果
     */
    @RequestMapping("/warehouse/deduct")
    DeductWarehouseResponse deductWareHouse(@RequestParam("commodityId") String commodityId,
                                            @RequestParam("orderId") String orderId,
                                            @RequestParam("count") Integer count);
}
