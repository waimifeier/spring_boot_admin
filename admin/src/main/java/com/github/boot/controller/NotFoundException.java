package com.github.boot.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.boot.beans.common.JSONReturn;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotFoundException implements ErrorController {
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
 
    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Object error(HttpServletRequest request) {
        Integer state = (Integer)request.getAttribute("javax.servlet.error.status_code");
        return JSONReturn.buildFailure(getStatusMessage(state), state);
    }

    private String getStatusMessage(Integer statusCode) {
        String defaultMessage = "服务器异常";
        if(ObjectUtil.isNull(statusCode))  return defaultMessage ;

        for (HttpStatus httpStatus : HttpStatus.values()) {
            if (statusCode == httpStatus.value()) return httpStatus.getReasonPhrase();
        }
        return defaultMessage;
    }
}