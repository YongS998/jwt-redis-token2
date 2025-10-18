package com.yongs.token2.pojo;

import lombok.Data;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/17 22:58
 */
@Data
public class Result {

    private Integer code;
    private String message;
    private Object data;

    private Result() {
    }

    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Object data){
        return new Result(200,"success",data);
    }

    public static Result success(){
        return success(null);
    }

    public static Result error(Integer code,String message){
        return new Result(code,message,null);
    }

    public static Result error(String message){
        return error(500,message);
    }

    public static Result error(){
        return error(null);
    }
}
