package com.tao.cloud.response;

import com.tao.cloud.config.ErrorType;

public class GetWarehouseResponse {

    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 剩余库存
     */
    private Integer warehouseCount;

    private ErrorType errMsg;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getWarehouseCount() {
        return warehouseCount;
    }

    public void setWarehouseCount(Integer warehouseCount) {
        this.warehouseCount = warehouseCount;
    }

    public ErrorType getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(ErrorType errMsg) {
        this.errMsg = errMsg;
    }

    public static GetWarehouseResponse createResponse(String commodityId, ErrorType errMsg) {
        GetWarehouseResponse response = new GetWarehouseResponse();
        response.setCommodityId(commodityId);
        response.setErrMsg(errMsg);
        return response;
    }

    public static GetWarehouseResponse createResponse(String commodityId, int count) {
        GetWarehouseResponse response = new GetWarehouseResponse();
        response.setCommodityId(commodityId);
        response.setWarehouseCount(count);
        return response;
    }
}
