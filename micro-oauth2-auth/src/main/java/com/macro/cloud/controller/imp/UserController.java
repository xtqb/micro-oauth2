package com.macro.cloud.controller.imp;

import com.macro.cloud.api.CommonResult;
import com.macro.cloud.controller.IUserController;
import com.macro.cloud.domain.UserDTO;
import com.macro.cloud.log.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于用户注册信息
 */
@RestController
@RequestMapping("/user")
public class UserController implements IUserController {
    @Autowired
    private LoggerUtils loggerUtils;

    // 用户注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Override
    public CommonResult register(@RequestBody UserDTO user) {
        loggerUtils.info("用户信息： " + user);
        return CommonResult.success("注册成功！");
    }

}
