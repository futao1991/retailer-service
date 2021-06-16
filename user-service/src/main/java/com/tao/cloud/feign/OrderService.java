package com.tao.cloud.feign;

import com.tao.cloud.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service")
public interface OrderService {

    @RequestMapping("/order/create")
    OrderResponse createOrder(@RequestParam("commodityId") String commodityId,
                              @RequestParam("orderId") String orderId,
                              @RequestParam("count") Integer count);
}
