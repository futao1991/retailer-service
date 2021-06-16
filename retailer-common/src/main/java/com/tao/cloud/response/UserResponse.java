package com.tao.cloud.response;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.model.User;

public class UserResponse {

    private User user;

    private ErrorType errMsg;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ErrorType getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(ErrorType errMsg) {
        this.errMsg = errMsg;
    }

    public static UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.user = user;
        return response;
    }

    public static UserResponse createUserResponse(User user, ErrorType errMsg) {
        UserResponse response = new UserResponse();
        response.user = user;
        response.errMsg = errMsg;
        return response;
    }
}
