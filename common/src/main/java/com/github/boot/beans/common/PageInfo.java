package com.github.boot.beans.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 响应带分页的数据
 */
@Data
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页数据
     */
    private PageData pageData;

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public PageInfo(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.pageData = new PageData(totalCount,pageSize, (int)Math.ceil((double)totalCount/pageSize),currPage);
    }

    public PageInfo(IPage<?> page) {
        this.list = page.getRecords();

        this.pageData = new PageData(
                (int)page.getTotal(),
                (int)page.getSize(),
                (int)page.getPages(),
                (int)page.getCurrent()
        );
    }

    public PageInfo(){}

    @Data
    public static class PageData implements Serializable {
        /**
         * 总记录数
         */
        private int totalCount;
        /**
         * 每页记录数
         */
        private int pageSize;
        /**
         * 总页数
         */
        private int totalPage;
        /**
         * 当前页数
         */
        private int currPage;

        public PageData() {
        }

        PageData(int totalCount, int pageSize, int totalPage, int currPage) {
            this.totalCount = totalCount;
            this.pageSize = pageSize;
            this.totalPage = totalPage;
            this.currPage = currPage;
        }
    }
}
