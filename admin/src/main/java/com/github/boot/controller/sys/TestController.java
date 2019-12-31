package com.github.boot.controller.sys;

import com.github.boot.beans.common.JSONReturn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@RestController
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    // 获取所有controller的请求
    @GetMapping("controller/urlMapping")
    public JSONReturn sysList(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取 url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String, Object>> urlList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = map.get(info);
         /*   PreAuthorize permissions = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            String perms = "";
            if (null != permissions) {
                String value = permissions.value();
                value = StringUtils.substringBetween(value, "hasAuthority(", ")");
                perms = StringUtils.join(value, ",");
            }*/
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            int length = methodParameters.length;


            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();

            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                Map<String, Object> urlMap = new HashMap<>();
                urlMap.put("url", url.replaceFirst("\\/", ""));
                urlMap.put("perms", methods);
                urlList.add(urlMap);
            }
        }
        return JSONReturn.buildSuccess(urlList);
    }
}
