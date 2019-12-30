package com.github.boot.beans.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  封装菜单树
 */
@Data
public class MenuNodeResponse extends TreeNodeResponse implements Serializable {

    private List<MenuNodeResponse> children;

    private List<CurrentPath> currentPath;  // 每个节点当前路径

    private Boolean disabled = false;

    private String addCode;

    public MenuNodeResponse(){}

    public MenuNodeResponse(Long id, String menuName, String menuType){
        super.setValue(id);
        super.setLabel(menuName);
        super.setCode(menuType);
    }



    @Data
    public static class CurrentPath implements Serializable {
        private Long id ;
        private String menuName;
    }
}
