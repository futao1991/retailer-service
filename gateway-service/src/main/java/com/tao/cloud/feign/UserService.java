package com.tao.cloud.feign;

import com.tao.cloud.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service")
public interface UserService {

    @RequestMapping("/user/getUserByName")
    UserResponse getUserByName(@RequestParam("userName") String userName);

    @RequestMapping("/user/checkUser")
    UserResponse checkUser(@RequestParam("userName") String userName,
                           @RequestParam("password") String password);
}
