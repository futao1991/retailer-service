package com.tao.cloud.service.impl;

import com.tao.cloud.config.ErrorType;
import com.tao.cloud.exception.BusinessException;
import com.tao.cloud.mapper.UserMapper;
import com.tao.cloud.model.User;
import com.tao.cloud.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 校验用户名密码
     * @param userName   用户名
     * @param password   密码(加密过后)
     * @return
     * @throws BusinessException
     */
    @Override
    public User checkUser(String userName, String password) throws BusinessException {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new BusinessException(ErrorType.ERROR_EMPTY_VALUE);
        }

        User user = userMapper.getUserByName(userName);
        if (null == user) {
            throw new BusinessException(ErrorType.ERROR_USER_NOTEXIST);
        }

        if (!StringUtils.equals(password, user.getPassword())) {
            throw new BusinessException(ErrorType.ERROR_PASSWORD_NOTCORRECT);
        }

        return user;
    }
}
