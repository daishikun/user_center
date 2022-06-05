package com.dsk.common;

public class ResultUtil {
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(0,"ok","",data);
    }

    public static BaseResponse error(Integer code,String message,String description){
       return new BaseResponse(code,message,description);
    }

    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),null);
    }

    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description,null);
    }
}
