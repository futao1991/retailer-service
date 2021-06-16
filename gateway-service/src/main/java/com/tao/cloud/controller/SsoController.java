package com.tao.cloud.controller;

import com.tao.cloud.feign.UserService;
import com.tao.cloud.model.User;
import com.tao.cloud.response.UserResponse;
import com.tao.cloud.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/sso")
public class SsoController {

    @Value("${token.expire-time}")
    private int tokenExpireTime;

    @Autowired
    private UserService userService;

    @RequestMapping("/loginPage")
    public ModelAndView loginPage(String url) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("url", url);
        return modelAndView;
    }

    @RequestMapping("/login")
    public String login(HttpServletResponse response, String username, String password, String url) {
        UserResponse userResponse = userService.checkUser(username, password);
        if (null != userResponse.getErrMsg()) {
            return userResponse.getErrMsg().getErrorMsg();
        }

        User user = userResponse.getUser();
        String token = JwtUtil.createJWT(tokenExpireTime * 1000, user);
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setMaxAge(tokenExpireTime);
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
