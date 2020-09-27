package com.project.p2p.handler;

import com.project.p2p.exception.ResultException;
import com.project.p2p.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof ResultException){
            ResultException resultException = (ResultException) e;
            Integer status = resultException.getStatus();
            if(status == null){
                status = 500;
            }
            return Result.fail(status, resultException);
        }else{
            e.printStackTrace();
            return null;
        }
    }
}
