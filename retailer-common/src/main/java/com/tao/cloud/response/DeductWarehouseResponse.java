package com.tao.cloud.response;

import com.tao.cloud.config.ErrorType;

public class DeductWarehouseResponse {

    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 处理结果
     */
    private String result;

    private ErrorType errMsg;


    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ErrorType getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(ErrorType errMsg) {
        this.errMsg = errMsg;
    }

    public DeductWarehouseResponse() {

    }

    public DeductWarehouseResponse(String commodityId, String orderId, String result) {
        this.commodityId = commodityId;
        this.orderId = orderId;
        this.result = result;
    }

    public DeductWarehouseResponse(String commodityId, ErrorType errMsg) {
        this.commodityId = commodityId;
        this.result = "Failed";
        this.errMsg = errMsg;
    }
}
