package com.github.boot.service.sys;


import com.github.boot.beans.request.sys.SysMenuParams;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.model.sys.SysMenu;

import java.util.List;

public interface MenuService {

    List<MenuNodeResponse> selectMenuList();

    void removeMenu(Long id);

    void saveMenu(SysMenuParams menu);

    void modifyMenu(SysMenuParams menu);

    List<MenuNodeResponse>  menuNodeChild(String menuType);

    SysMenu menuDetail(Long id);
}
