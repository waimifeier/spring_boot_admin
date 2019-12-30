package com.github.boot.beans.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.boot.constant.ConstantQueryParams;

import java.util.Map;

/**
 * 处理默认分页条数
 */

public class QueryPage<T>  {

    private QueryPage(){}

    public  static<T> IPage<T> getInstance(Map<String, Object> params) {
        //分页参数
        Integer curPage = 1;
        Integer limit = 10;

        if(params.get(ConstantQueryParams.PAGE_NUM) != null){
            curPage = (Integer) params.get(ConstantQueryParams.PAGE_NUM);
        }
        if(params.get(ConstantQueryParams.PAGE_SIZE) != null){
            limit = (Integer) params.get(ConstantQueryParams.PAGE_SIZE);
        }
        //分页对象
        return new Page<>(curPage, limit);
    }

}