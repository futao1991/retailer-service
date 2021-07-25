package com.tao.cloud.payservice.feign;

import com.tao.cloud.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "order-service")
public interface OrderService {

    @RequestMapping("/order/delete")
    OrderResponse deleteOrder(@RequestParam("orderId") String orderId);
}
