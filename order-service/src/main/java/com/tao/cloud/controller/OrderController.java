package com.tao.cloud.controller;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.response.OrderResponse;
import com.tao.cloud.service.OrderService;
import com.tao.cloud.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单API接口
     * @param commodityId  商品id
     * @param orderId      订单id
     * @param count        购买数量
     * @return             订单信息
     */
    @RequestMapping("/create")
    @ResponseBody
    public OrderResponse createOrder(@RequestParam("commodityId") String commodityId,
                                     @RequestParam("orderId") String orderId,
                                     @RequestParam("count") Integer count) {
        try {
            OrderMessage orderMessage = orderService.createOrder(orderId, commodityId, count);
            orderMessage.setUserId(TokenUtil.getUserIdFromAuthorizationToken());
            return OrderResponse.createOrderResponse(orderMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorType.ERROR_INTERNAL.getErrorCode(), e.getMessage());
        }
    }
}
