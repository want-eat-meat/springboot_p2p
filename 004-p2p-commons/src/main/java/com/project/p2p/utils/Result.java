package com.project.p2p.utils;

import com.project.p2p.exception.ResultException;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.crypto.Data;

public class Result {
    private int status;
    private String msg;
    private Object data;

    private Result(int status, String msg, Object data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private Result(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public static Result success(Object data){
        return new Result(200, "success", data);
    }

    public static Result success(){
        return new Result(200, "success");
    }

    public static Result fail(int status, ResultException e){
        return new Result(status, e.getMessage());
    }

    public static Result fail(int status, String msg){
        return new Result(status, msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
