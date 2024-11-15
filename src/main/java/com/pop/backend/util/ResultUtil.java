package com.pop.backend.util;

/**
 *
 * @author yl
 * @since 2024-10-22
 */
public class ResultUtil {

    private Integer code;

    private Boolean status; //true,false

    private String msg;

    private Object data;


    private ResultUtil() {
    }

    private ResultUtil(Integer code, Boolean status, String msg, Object data) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }


    public static ResultUtil isSuccess(Object data){
        return new ResultUtil(20000,true,"succeed",data);
    }


    public static ResultUtil isSuccess(String msg,Object data){
        return new ResultUtil(20000,true,msg,data);
    }


    public static ResultUtil isFail(Integer code,String msg){
        return new ResultUtil(code,false,msg,null);
    }
}
