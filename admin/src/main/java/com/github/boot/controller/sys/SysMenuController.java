package com.github.boot.controller.sys;

import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.sys.SysMenuParams;
import com.github.boot.model.sys.SysMenu;
import com.github.boot.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统菜单控制器
 */

@RestController
@RequestMapping("/sys_menu")
public class SysMenuController {

    @Autowired
    private MenuService menuService;

    /**
     *  1. 查看列表
     * @return
     */
    @RequestMapping("/list")
    public JSONReturn menuList() {
        return JSONReturn.buildSuccess(menuService.selectMenuList());
    }

    /**
     * 2. 删除
     * @param id
     * @return
     */
    @RequestMapping("/remove/{id:\\d+}")
    public JSONReturn removeMenu(@PathVariable("id") Long id) {
        menuService.removeMenu(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3. 保存菜单`-
     * @param menu
     * @return
     */
    @RequestMapping("/save")
    public JSONReturn saveMenu(@RequestBody @Validated SysMenuParams menu) {
        menuService.saveMenu(menu);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 4. 修改菜单
     * @param menu
     * @return
     */
    @RequestMapping("/modify")
    public JSONReturn modifyMenu(@RequestBody @Validated SysMenuParams menu) {
        menuService.modifyMenu(menu);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 5.根据id获取 所有菜单节点
     * @return
     */
    @RequestMapping("/menu_node")
    public JSONReturn menuNodeChildById(@RequestBody SysMenu menu) {
        return JSONReturn.buildSuccess(menuService.menuNodeChild(menu.getMenuType()));
    }


    /**
     * 6.查看详情
     * @return
     */
    @RequestMapping("/detail/{id:\\d+}")
    public JSONReturn menuDetail(@PathVariable Long id) {
        return JSONReturn.buildSuccess(menuService.menuDetail(id));
    }
}
