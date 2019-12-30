package com.github.boot.beans.response.sys;

import lombok.Data;

import java.util.List;

/**
 * 部门添加修改参数
 */
@Data
public class DepartmentDetailResponse {

    private Long id;
    private String code;  // 可选值 ： COMPANY,DEPARTMENT
    private String name; // 公司名称 或 不猛名称
    private String address; // 公司地址
    private Long agentId; // 部门负责人或公司负责人
    private String agentUser;
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
