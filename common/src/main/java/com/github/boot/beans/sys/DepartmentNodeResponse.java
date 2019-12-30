package com.github.boot.beans.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DepartmentNodeResponse extends TreeNodeResponse implements Serializable {

    private List<DepartmentNodeResponse> children;
    private String addCode;
    private Boolean disabled;
    private List<Long> path;



    public DepartmentNodeResponse(){

    }

    public DepartmentNodeResponse(List<Long> path){
        this.path = path;
    }

}
