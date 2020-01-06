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
 * 公司部门职位表
 * </p>
 *
 * @author dlj
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartmentPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private Date createTime;

    private Long createUser;

    /**
     * 职位名称
     */
    private String position;

    /**
     * 是否删除 1 删除 0 正常
     */
    private Boolean deleted;

    private Long departmentId;


}
