package com.tao.cloud.payservice.controller;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.payservice.service.MQService;
import com.tao.cloud.payservice.util.TokenUtil;
import com.tao.cloud.response.OrderResponse;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private MQService mqService;

    @Autowired
    private TransactionMQProducer producer;

    @RequestMapping("/payorder")
    @ResponseBody
    public OrderResponse payOrder(@RequestParam("orderId") String orderId) {
        try {
            Long userId = TokenUtil.getUserIdFromAuthorizationToken();
            OrderMessage orderMessage = mqService.payOrder(orderId, userId);
            return OrderResponse.createOrderResponse(orderMessage);
        } catch (BusinessException e) {
            return OrderResponse.createOrderResponse(e.getErrorType());
        } catch (Exception e) {
            throw new BusinessException(ErrorType.ERROR_INTERNAL.getErrorCode(), e.getMessage());
        }
    }

}
