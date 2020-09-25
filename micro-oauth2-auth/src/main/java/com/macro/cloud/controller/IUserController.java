package com.macro.cloud.controller;

import com.macro.cloud.api.CommonResult;
import com.macro.cloud.domain.UserDTO;

public interface IUserController {
    CommonResult register(UserDTO user);
}
