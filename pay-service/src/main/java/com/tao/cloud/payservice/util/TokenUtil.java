package com.tao.cloud.payservice.util;

import com.tao.cloud.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    /**
     * 从token中获取userId
     * @return userId
     */
    public static Long getUserIdFromAuthorizationToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader("authorization");
            if (StringUtils.isNotEmpty(authorization)) {
                String accessToken = StringUtils.substringAfter(authorization, "Bearer ");
                return JwtUtil.getUserIdFromToken(accessToken);
            }
        }
        return null;
    }
}