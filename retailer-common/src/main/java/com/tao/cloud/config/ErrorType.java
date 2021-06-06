package com.tao.cloud.config;

public enum ErrorType {

    ERROR_WAREHOUSE_NOTEXIST("ERROR_WAREHOUSE_NOTEXIST", "The commodity not exist."),

    ERROR_NOT_ENOUGH_WAREHOUSE("ERROR_NOT_ENOUGH_WAREHOUSE", "The warehouse is not enough."),

    ERROR_CREATE_ORDER("ERROR_CREATE_ORDER", "Create Order Failed"),

    ERROR_INTERNAL("ERROR_INTERNAL", "Internal Error");

    private String errorCode;

    private String errorMsg;

    ErrorType(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
