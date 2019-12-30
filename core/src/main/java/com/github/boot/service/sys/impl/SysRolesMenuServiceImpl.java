package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysRolesMenuMapper;
import com.github.boot.model.sys.SysRolesMenu;
import com.github.boot.service.sys.SysRolesMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单管理表 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
public class SysRolesMenuServiceImpl extends ServiceImpl<SysRolesMenuMapper, SysRolesMenu> implements SysRolesMenuService {


    @Override
    public List<Long> queryRoleMenuId(List<Long> sysRolesId) {
        LambdaQueryWrapper<SysRolesMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRolesMenu::getSysRoleId,sysRolesId).eq(SysRolesMenu::getDeleted,false);
        List<SysRolesMenu> sysRolesMenus = baseMapper.selectList(queryWrapper);

        return CollectionUtil.isEmpty(sysRolesMenus) ? new ArrayList<>() :
                sysRolesMenus.stream().map(SysRolesMenu::getSysMenuId).collect(Collectors.toList());
    }
}
