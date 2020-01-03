package com.github.boot.controller.sys;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.boot.annotation.SysLog;
import com.github.boot.utils.poi.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    // 获取所有controller的请求
    @GetMapping("controller/urlMapping")
    @SysLog("获取所有映射请求")
    public void sysList(HttpServletResponse response){
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

        try {
            HashMap<String,String> header = new HashMap<>();
            header.put("url", "地址");
            header.put("perms","方式");
            ExcelUtils.exportResponse(response,header,urlList,"一班成绩单");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
