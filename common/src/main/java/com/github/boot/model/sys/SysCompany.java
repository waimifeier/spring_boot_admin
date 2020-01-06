package com.github.boot.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 公司架构表
 * </p>
 *
 * @author dlj
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 负责人
     */
    private Long agentId;

    /**
     * 地址
     */
    private String address;

    /**
     * 1删除 0 正常
     */
    private Boolean deleted;

    /**
     * 父公司id
     */
    private Long parentId;


}
