package com.tao.cloud.config;

public enum ErrorType {

    ERROR_WAREHOUSE_NOTEXIST("ERROR_WAREHOUSE_NOTEXIST", "The commodity not exist."),

    ERROR_NOT_ENOUGH_WAREHOUSE("ERROR_NOT_ENOUGH_WAREHOUSE", "The warehouse is not enough."),

    ERROR_CREATE_ORDER("ERROR_CREATE_ORDER", "Create Order Failed"),

    ERROR_USER_NOTEXIST("ERROR_USER_NOTEXIST", "The user not exist."),

    ERROR_EMPTY_VALUE("ERROR_EMPTY_VALUE", "The username or password is empty"),

    ERROR_PASSWORD_NOTCORRECT("ERROR_PASSWORD_NOTEQUAL", "The password is not correct."),

    ERROR_ORDER_NOTEXIST("ERROR_ORDER_NOTEXIST", "The order message not exist."),

    ERROR_USER_NOTOWNER("ERROR_USER_NOTOWNER", "The order is not belong to this user"),

    ERROR_MONEY_NOTENOUGH("ERROR_MONEY_NOTENOUGH", "The money is not enough."),

    ERROR_SEND_MQ("ERROR_SEND_MQ", "Send message to mq failed."),

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
