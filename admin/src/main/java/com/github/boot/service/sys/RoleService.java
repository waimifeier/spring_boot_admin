package com.github.boot.service.sys;


import com.github.boot.beans.common.PageInfo;
import com.github.boot.beans.request.sys.EditorRolesParams;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.beans.sys.RoleInfoResponse;

import java.util.HashMap;
import java.util.List;

public interface RoleService {

    PageInfo getRoleList(HashMap<String, Object> params);

    void removeRolesById(Long id);

    RoleInfoResponse roleInfo(Long id);

    List<MenuNodeResponse> menuList();

    void saveRoles(EditorRolesParams rolesParams);

    void modifyRoles(EditorRolesParams rolesParams);
}
