package com.github.boot.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.beans.sys.BasicSysUser;
import com.github.boot.dao.sys.SysUserRolesMapper;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.model.sys.SysUser;
import com.github.boot.model.sys.SysUserRoles;
import com.github.boot.service.sys.SysUserRolesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色用户关联表(一个用户多个角色) 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
public class SysUserRolesServiceImpl extends ServiceImpl<SysUserRolesMapper, SysUserRoles> implements SysUserRolesService {

    @Override
    public List<BasicSysUser> selectUserListByRoleId(Long rolesId) {

        List<SysUser> sysUsers = baseMapper.selectUserListByRoleId(rolesId);

        return sysUsers.stream().map(item->{
            BasicSysUser basicSysUser = new BasicSysUser();
            basicSysUser.setAccount(item.getAccount());
            basicSysUser.setId(item.getId());
            basicSysUser.setNickName(item.getNickName());
            basicSysUser.setPhone(item.getPhone());
            basicSysUser.setPhoto(item.getPhoto());
            return basicSysUser;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysRoles> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public void deleteAllRoles(Long userId) {
        SysUserRoles roles = new SysUserRoles().setDeleted(true);
        LambdaQueryWrapper<SysUserRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRoles::getSysUserId,userId);
        baseMapper.update(roles,queryWrapper);
    }
}
