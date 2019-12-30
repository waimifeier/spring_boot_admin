package com.github.boot.service.sys;

import com.github.boot.beans.common.PageInfo;
import com.github.boot.beans.request.sys.RoleUserMappingParams;
import com.github.boot.beans.request.sys.SysUserParams;
import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.model.sys.SysUser;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    PageInfo userList(HashMap<String, Object> params);

    void disabledUser(Long id);

    void removeUser(Long id);

    void saveUser(SysUser sysUser, SysUserParams params);

    void modifyUser(SysUser sysUser, SysUserParams params);

    HashMap<String,Object> badge();

    List<HashMap<String,Object>> roleList();

    void roleMapping(RoleUserMappingParams params);

    List<DepartmentNodeResponse> department();
}
