package com.github.boot.beans.request.sys;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户关联角色
 */
@Data
public class RoleUserMappingParams implements Serializable{

    @NotNull(message = "用户id为空")
    private Long userId;

    private List<Long> rid;
}
