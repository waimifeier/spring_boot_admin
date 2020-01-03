package com.github.boot.utils.poi;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExcelUtils {

    private ExcelUtils(){}

    /**
     * 导出excel写出浏览器
     * @param response
     *
     * @param data
     * @param fileName
     */
    public static void exportResponse(HttpServletResponse response, HashMap<String,String> headers ,
                                                List<?> data, String fileName) throws IOException{
        ExcelWriter writer = ExcelUtil.getWriter();
        // 修改sheet名称
        writer.renameSheet(fileName);
        // 合并第一行单元格
        writer.merge(headers.size()-1,fileName);

        // 设置表头内容
        writer.setHeaderAlias(headers);

        // 设置表头行高
        writer.setRowHeight(0,18);

      /*
        行宽
        writer.setColumnWidth(0,20);
        writer.setColumnWidth(1, 60);*/

        writer.write(data);

        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
               + java.net.URLEncoder.encode(fileName+".xls", "UTF-8"));

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out);
        writer.close();
        out.close();
    }

}
