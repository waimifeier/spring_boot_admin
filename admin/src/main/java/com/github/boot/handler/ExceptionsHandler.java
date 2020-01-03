package com.github.boot.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.common.PlantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@ResponseBody
@ResponseStatus(HttpStatus.OK)
public class ExceptionsHandler {

    @ExceptionHandler({PlantException.class}) //业务异常
    public JSONReturn plantException(Exception e) {
        e.printStackTrace();
        PlantException ytxException = (PlantException) e;
        return JSONReturn.buildFailure(ytxException.getMessage(),ytxException.getCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public JSONReturn handlerNoFoundException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("路径不存在，请检查路径是否正确",HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({BindException.class}) //参数传入异常
    public JSONReturn bindException(Exception e) {
        e.printStackTrace();
        BindException exception = (BindException) e;
        return JSONReturn.buildFailure(exception.getFieldError().getDefaultMessage());
    }


    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public JSONReturn httpRequestMethodNotSupportedException(Exception e) {
        e.printStackTrace();
        String method = ((HttpRequestMethodNotSupportedException) e).getMethod();
        return JSONReturn.buildFailure(method+"请求方式不正确");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public JSONReturn missingServletRequestParameterException(Exception e) {
        e.printStackTrace();
        String paramName = ((MissingServletRequestParameterException) e).getParameterName();
        return JSONReturn.buildFailure("缺少参数" + paramName);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public JSONReturn httpMediaTypeNotSupportedException(Exception e) {
        e.printStackTrace();
        String paramName =e.getMessage();
        return JSONReturn.buildFailure("请求参数类型错误["+paramName+"]");
    }

    @ExceptionHandler({JsonParseException.class})
    public JSONReturn jsonParseException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("json解析异常");
    }

    @ExceptionHandler({NumberFormatException.class})
    public JSONReturn numberFormatException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("数字类型转换错误");
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public JSONReturn httpMessageNotReadableException(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("请求参数格式错误");
    }

    @ExceptionHandler({Exception.class})
    public JSONReturn exceptionHandler(Exception e) {
        e.printStackTrace();
        return JSONReturn.buildFailure("服务器异常");
    }

}