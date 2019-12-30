package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysRoles;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysRolesService extends IService<SysRoles> {

    List<SysRoles> allList();
}
