package com.tao.cloud.service;

import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.model.User;

public interface UserService {

    User checkUser(String userName, String password) throws BusinessException;
}
