package com.tao.cloud.response;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.model.Commodity;

public class GetWarehouseResponse {

    /**
     * 商品
     */
    private Commodity commodity;

    private ErrorType errMsg;

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public ErrorType getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(ErrorType errMsg) {
        this.errMsg = errMsg;
    }

    public static GetWarehouseResponse createResponse(Commodity commodity, ErrorType errMsg) {
        GetWarehouseResponse response = new GetWarehouseResponse();
        response.setCommodity(commodity);
        response.setErrMsg(errMsg);
        return response;
    }
}
