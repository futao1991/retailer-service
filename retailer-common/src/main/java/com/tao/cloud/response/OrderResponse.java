package com.tao.cloud.response;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.config.OrderMessage;

public class OrderResponse {

    private OrderMessage orderMessage;

    private ErrorType errMsg;

    public OrderResponse() {

    }

    public static OrderResponse createOrderResponse(OrderMessage orderMessage) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.orderMessage = orderMessage;
        return orderResponse;
    }

    public static OrderResponse createOrderResponse(ErrorType errMsg) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.errMsg = errMsg;
        return orderResponse;
    }

    public OrderMessage getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(OrderMessage orderMessage) {
        this.orderMessage = orderMessage;
    }

    public ErrorType getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(ErrorType errMsg) {
        this.errMsg = errMsg;
    }
}
