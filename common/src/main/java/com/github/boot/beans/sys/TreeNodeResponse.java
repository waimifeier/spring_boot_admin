package com.github.boot.beans.sys;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据elmentUI字段返回
 */
@Data
public class TreeNodeResponse implements Serializable {
    private Long value ; // id
    private String code ; // 类型
    private String label; // 名称
}
