package com.github.boot.beans.request.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 部门添加修改参数
 */
@Data
public class DepartmentRequestParams  {

    private Long id;

    @NotEmpty(message = "类型不能为空~")
    private String code;  // 可选值 ： COMPANY,DEPARTMENT

    @NotNull(message = "请选择上级")
    private Long parentId; // 上级部门，或上级公司

    @NotEmpty(message = "上级类型不能为空~")
    private String parentCode; // 上级部门或公司的code

    @NotEmpty(message = "名称不能为空")
    private String name; // 公司名称 或 不猛名称

    private String address; // 公司地址

    @NotNull(message = "请选择负责人")
    private Long agentId; // 部门负责人或公司负责人

    private List<Contacts> contacts; // 联系人

    private List<Position> positions;

    @Data
    public static class Contacts{
        private Long id;
        private String contacts;
        private String contactNumber;
    }

    @Data
    public static class Position{
        private Long id;
        private String positionName; // 职位名称
    }


}
