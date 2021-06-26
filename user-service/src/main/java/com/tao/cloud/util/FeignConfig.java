package com.tao.cloud.util;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader("authorization");
            if (StringUtils.isNotEmpty(authorization)) {
                requestTemplate.header("authorization", authorization);
            }
        }
    }
}
