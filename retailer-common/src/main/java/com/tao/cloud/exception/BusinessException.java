package com.tao.cloud.exception;

import com.tao.cloud.config.ErrorType;

public class BusinessException extends RuntimeException {

    private ErrorType errorType;

    private String errorCode;

    private String errorMsg;

    public BusinessException(ErrorType errorType) {
        super(errorType.getErrorMsg());
        this.errorType = errorType;
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
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
