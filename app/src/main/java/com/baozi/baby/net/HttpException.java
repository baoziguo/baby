package com.baozi.baby.net;

public class HttpException extends Exception {
    private String code;
    private String msg;
    public HttpException(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
