package com.github.boot.controller.sys;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.boot.annotation.SysLog;
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

        ExcelWriter bigWriter = ExcelUtil.getBigWriter();

        bigWriter.merge(1, "一班成绩单");
        bigWriter.write(urlList);
        bigWriter.writeCellValue(0, urlList.size()+3,"合计");
        bigWriter.writeCellValue(1, urlList.size()+3,urlList.size()-1);



        response.setContentType("application/vnd.ms-excel;charset=utf-8");
       //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bigWriter.flush(out);
        // 关闭writer，释放内存
        bigWriter.close();
    }
}
