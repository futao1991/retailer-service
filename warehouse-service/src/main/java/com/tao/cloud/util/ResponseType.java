package com.tao.cloud.util;

public enum ResponseType {

    ERROR_COMMODITY_NOT_EXIST("ERROR_COMMODITY_NOT_EXIST"),

    ERROR_NOT_ENOUGH_WAREHOUSE("ERROR_NOT_ENOUGH_WAREHOUSE");

    private String errorMsg;

    private ResponseType(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
