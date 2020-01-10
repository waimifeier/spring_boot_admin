package com.github.boot.configure.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.information.beans.BusinessException;
import com.information.beans.JSONReturn;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
@ResponseBody
@ResponseStatus(HttpStatus.OK)
public class ExceptionsHandler {

    @ExceptionHandler({BusinessException.class}) //业务异常
    public JSONReturn plantException(Exception e) {
        e.printStackTrace();
        BusinessException ytxException = (BusinessException) e;
        return JSONReturn.buildFailure(ytxException.getMessage());
    }

    @ExceptionHandler({BindException.class}) //参数传入异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONReturn bindException(Exception e) {
        e.printStackTrace();
        BindException exception = (BindException) e;
        return JSONReturn.buildFailure(exception.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JSONReturn methodArgumentNotValidException(Exception e) {
        e.printStackTrace();
        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
        BindingResult result = exception.getBindingResult();
        return JSONReturn.buildFailure(result.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public JSONReturn httpRequestMethodNotSupportedException(Exception e) {
        e.printStackTrace();
        String method = ((HttpRequestMethodNotSupportedException) e).getMethod();
        return JSONReturn.buildFailure(method + "请求方式不正确");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONReturn missingServletRequestParameterException(Exception e) {
        e.printStackTrace();
        String paramName = ((MissingServletRequestParameterException) e).getParameterName();
        return JSONReturn.buildFailure( "缺少参数" + paramName);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONReturn httpMediaTypeNotSupportedException(Exception e) {
        e.printStackTrace();
        String paramName =e.getMessage();
        return JSONReturn.buildFailure("请求体类型错误 " + paramName);
    }

    @ExceptionHandler({JsonParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONReturn jsonParseException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("json解析异常");
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public JSONReturn constraintViolationException(Exception e) {
        e.printStackTrace();
        String msg = e.getMessage();
        return JSONReturn.buildFailure("约束冲突");
    }

    @ExceptionHandler({NumberFormatException.class})
    public JSONReturn numberFormatException(Exception e) {
        e.printStackTrace();
        String msg = e.getMessage();
        return JSONReturn.buildFailure("数字类型转换错误");
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public JSONReturn httpMessageNotReadableException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("参数解析错误");
    }

    @ExceptionHandler({Exception.class})
    public JSONReturn exceptionHandler(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("服务器开小差了");
    }


}