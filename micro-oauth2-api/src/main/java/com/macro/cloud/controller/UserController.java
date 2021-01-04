package com.macro.cloud.controller;

import com.macro.cloud.domain.UserDTO;
import com.macro.cloud.holder.LoginUserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取登录用户信息接口
 * Created by macro on 2020/6/19.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginUserHolder loginUserHolder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/currentUser")
    public UserDTO currentUser() {
        logger.info("0000000000000000000000000000000");
        return loginUserHolder.getCurrentUser();
    }

}
