package com.dsk.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseResponse",description = "公共返回结果")
public class BaseResponse<T> {

    @ApiModelProperty(value = "code码",required = true)
    private Integer code;

    @ApiModelProperty(value = "信息",required = true)
    private String message;


    @ApiModelProperty(value = "详细信息",required = true)
    private String description;



    @ApiModelProperty(value = "返回信息")
    private T data;

    public BaseResponse(){

    }

    public BaseResponse(Integer code, String message,String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
    }

    public BaseResponse(Integer code, String message,String description) {
       this(code,message,description,null);
    }

    public BaseResponse(Integer code,T data) {
        this(code,"","",data);
    }
}
