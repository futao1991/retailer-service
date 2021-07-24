package com.tao.cloud.util;

import com.auth0.jwt.interfaces.Claim;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.tao.cloud.feign.UserService;
import com.tao.cloud.response.UserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class AccessFilter extends ZuulFilter {

    @Value("${token.expire-time}")
    private int tokenExpireTime;

    @Autowired
    private UserService userService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private String resolveGetUrl(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        if ("GET".equals(method)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                for (String value : values) {
                    list.add(key + "=" + value);
                }
            }
            if (!list.isEmpty()) {
                url = url + "?" + StringUtils.join(list, "&");
            }
        }
        return url;
    }

    private boolean checkToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        }

        Map<String, Claim> claimMap = null;
        try {
            claimMap = JwtUtil.check(accessToken);
        } catch (Exception e) {
            return false;
        }

        String userName = claimMap.get("username").asString();
        String password = claimMap.get("password").asString();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return false;
        }

        UserResponse userResponse = userService.checkUser(userName, password);
        if (null != userResponse.getErrMsg()) {
            return false;
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        String url = request.getRequestURL().toString();
        String accessToken = null;
        String authorization = request.getHeader("authorization");
        if (StringUtils.isNotEmpty(authorization)) {
            accessToken = StringUtils.substringAfter(authorization, "Bearer ");
        }
        Cookie[] cookies = request.getCookies();
        Cookie tokenCookie = null;
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    tokenCookie = cookie;
                }
            }
        }

        if (url.contains("sso/loginPage") || url.contains("sso/login") || checkToken(accessToken)) {
            ctx.setSendZuulResponse(true);
            if (null != accessToken) {
                ctx.addZuulRequestHeader("authorization", "Bearer " + accessToken);
            }
            if (null != tokenCookie) {
                Long remainTime = JwtUtil.getRemainTimeInToken(accessToken);
                //token过期前30秒内刷新token
                if (remainTime > 0 && remainTime <= 30000) {
                    tokenCookie.setValue(JwtUtil.refreshToken(accessToken, tokenExpireTime));
                    tokenCookie.setMaxAge(tokenExpireTime);
                    tokenCookie.setPath("/");
                    response.addCookie(tokenCookie);
                }
            }
            ctx.setResponseStatusCode(200);
            return null;
        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                response.sendRedirect("/sso/loginPage?url=" + resolveGetUrl(request));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
