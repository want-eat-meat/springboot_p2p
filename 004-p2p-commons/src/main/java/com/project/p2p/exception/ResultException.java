package com.project.p2p.exception;

public class ResultException extends RuntimeException {
    private Integer status;

    private String message;

    public ResultException(String message){
        this.message = message;
    }

    public ResultException(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
