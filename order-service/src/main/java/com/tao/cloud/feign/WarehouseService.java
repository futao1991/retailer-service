package com.tao.cloud.feign;

import com.tao.cloud.response.DeductWarehouseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "warehouse-service")
public interface WarehouseService {

    /**
     * 扣减某件商品的库存
     * @param commodityId  商品id
     * @param count        待扣减的数量
     * @return             本次扣减结果
     */
    @RequestMapping("/warehouse/deduct")
    DeductWarehouseResponse deductWareHouse(@RequestParam("commodityId") String commodityId,
                                            @RequestParam("count") Integer count);

    /**
     * 恢复某件商品的库存
     * @param commodityId  商品id
     * @return             操作结果
     */
    @RequestMapping("/warehouse/restore")
    DeductWarehouseResponse restoreWarehouse(@RequestParam("commodityId") String commodityId,
                                             @RequestParam("orderId") String orderId);
}
