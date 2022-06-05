package com.dsk.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserLoginRequest",description = "用户登录请求参数")
public class UserLoginRequest {

    @ApiModelProperty(value = "账户",required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码",required = true)
    private String userPassword;
}
