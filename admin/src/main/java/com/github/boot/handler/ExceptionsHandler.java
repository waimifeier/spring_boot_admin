package com.github.boot.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.boot.beans.common.PlantException;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({PlantException.class}) //业务异常
    public String plantException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        PlantException ytxException = (PlantException) e;
        return setFailMessage(request, ytxException.getMessage());
    }


    @ExceptionHandler({BindException.class}) //参数传入异常
    public String bindException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        BindException exception = (BindException) e;
        return setFailMessage(request, exception.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public String methodArgumentNotValidException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
        BindingResult result = exception.getBindingResult();
        return setFailMessage(request, result.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public String httpRequestMethodNotSupportedException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String method = ((HttpRequestMethodNotSupportedException) e).getMethod();
        return setFailMessage(request, method + "请求方式不正确");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String missingServletRequestParameterException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String paramName = ((MissingServletRequestParameterException) e).getParameterName();
        return setFailMessage(request, "缺少参数" + paramName);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public String httpMediaTypeNotSupportedException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String paramName =e.getMessage();
        return setFailMessage(request, "请求体类型错误 " + paramName);
    }

    @ExceptionHandler({JsonParseException.class})
    public String jsonParseException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return setFailMessage(request, "json解析异常");
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public String constraintViolationException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String msg = e.getMessage();
        return setFailMessage(request, "约束冲突" + msg);
    }

    @ExceptionHandler({NumberFormatException.class})
    public String numberFormatException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        String msg = e.getMessage();
        return setFailMessage(request, "数字类型转换错误" + msg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public String httpMessageNotReadableException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return setFailMessage(request, "参数解析错误");
    }

    @ExceptionHandler({Exception.class})
    public String exceptionHandler(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return setFailMessage(request, "服务器开小差了");
    }


    private String setFailMessage(HttpServletRequest request, String msg) {
        String acceptHeader = request.getHeader("Accept");

        if(acceptHeader.contains("text/html")){
            request.setAttribute("javax.servlet.error.status_code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        request.setAttribute("javax.servlet.error.message", msg);
        return "forward:/error";
    }

}