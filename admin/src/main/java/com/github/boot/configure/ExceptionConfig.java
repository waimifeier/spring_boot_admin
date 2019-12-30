package com.github.boot.configure;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class ExceptionConfig extends DefaultErrorAttributes {

   @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        Map<String, Object> errorAttributes = new LinkedHashMap();
        Integer statusCode = (Integer) webRequest.getAttribute("javax.servlet.error.status_code", 0);
        errorAttributes.put("head", includeStackTrace);
        errorAttributes.put("content", "");
        errorAttributes.put("status", statusCode == 200 ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode);
        Object msg = webRequest.getAttribute("javax.servlet.error.message", 0);
        errorAttributes.put("message", ObjectUtil.isNull(msg)? getStatusMessage(statusCode):msg);
        return errorAttributes;
    }


    private String getStatusMessage(Integer statusCode) {
        if(ObjectUtil.isNull(statusCode))  return "服务器开小差了" ;

        for (HttpStatus httpStatus : HttpStatus.values()) {
            if (statusCode == httpStatus.value()) return httpStatus.getReasonPhrase();
        }
        return "服务器开小差了";
    }
}
