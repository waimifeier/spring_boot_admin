package com.github.boot.controller.sys;

import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.sys.SysMenuParams;
import com.github.boot.model.sys.SysMenu;
import com.github.boot.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 系统菜单控制器
 */

@RestController
@RequestMapping("/sys_menu")
public class SysMenuController {

    @Resource
    private MenuService menuService;

    /**
     *  1. 查看列表
     * @return
     */
    @PostMapping("/list")
    public JSONReturn menuList() {
        return JSONReturn.buildSuccess(menuService.selectMenuList());
    }

    /**
     * 2. 删除
     * @param id
     * @return
     */
    @PostMapping("/remove/{id:\\d+}")
    public JSONReturn removeMenu(@PathVariable("id") Long id) {
        menuService.removeMenu(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3. 保存菜单
     * @param menu
     * @return
     */
    @PostMapping("/save")
    public JSONReturn saveMenu(@RequestBody @Validated SysMenuParams menu) {
        menuService.saveMenu(menu);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 4. 修改菜单
     * @param menu
     * @return
     */
    @PostMapping("/modify")
    public JSONReturn modifyMenu(@RequestBody @Validated SysMenuParams menu) {
        menuService.modifyMenu(menu);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 5.根据id获取 所有菜单节点
     * @return
     */
    @PostMapping("/menu_node")
    public JSONReturn menuNodeChildById(@RequestBody SysMenu menu) {
        return JSONReturn.buildSuccess(menuService.menuNodeChild(menu.getMenuType()));
    }


    /**
     * 6.查看详情
     * @return
     */
    @PostMapping("/detail/{id:\\d+}")
    public JSONReturn menuDetail(@PathVariable Long id) {
        return JSONReturn.buildSuccess(menuService.menuDetail(id));
    }
}
