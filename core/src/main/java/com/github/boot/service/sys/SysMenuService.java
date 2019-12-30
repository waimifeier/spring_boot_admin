package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.beans.sys.RoleInfoResponse;
import com.github.boot.model.sys.SysMenu;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysMenuService extends IService<SysMenu> {

  /**
     * 根据菜单id 构建 tree节点
     * @param parentId 父id
     * @param menuIds 从这里的id里面查找 构建一个节点
     * @param needButton 是否需要按钮节点
     * @return
     */
    List<RoleInfoResponse.BasicMenu> buildMenuNodeByMenuIds(Long parentId, List<Long> menuIds, Boolean needButton);


    List<String> buildSingleNodePermission(Long parentId, List<Long> childIds);

    /**
     * 查询指定菜单下的子菜单
     * @param id
     * @return
     */
    List<SysMenu> queryChildSysMenu(Long id);

    /**
     * 构建菜单节点
     * @param rootNode
     * @param menuType
     * @param available
     * @return
     */
    List<MenuNodeResponse> buildMenuNode(MenuNodeResponse rootNode, String menuType, String available);
}
