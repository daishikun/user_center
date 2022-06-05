package com.dsk.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserRegisterRequest",description = "用户注册请求参数")
public class UserRegisterRequest {


    @ApiModelProperty(value = "账户",required = true)
    private String userAccount;

    @ApiModelProperty(value = "密码",required = true)
    private String userPassword;

    @ApiModelProperty(value = "验证密码",required = true)
    private String checkPassword;
}
