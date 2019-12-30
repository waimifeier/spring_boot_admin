package com.github.boot.beans.response.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class SysUserListResponse implements Serializable {

    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    private String account;


    private String photo;

    private Date loginTime;

    /**
     * 昵称
     */
    private String nickName;

    private String phone;

    private Integer sex;

    private Integer accountType;

    /**
     * 状态：0 正常，1禁用，-1 删除
     */
    private Integer state;

    private List<Map<String,Object>> roles;

    private List<Long> departmentArr ; // 职位

    private String companyName;

    private String positionName;

    private String departmentName;

}
