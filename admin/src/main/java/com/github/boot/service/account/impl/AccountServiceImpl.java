package com.github.boot.service.account.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.request.account.ModifyUserPasswordParams;
import com.github.boot.beans.request.account.UserLoginParams;
import com.github.boot.beans.response.account.BackendTokenBean;
import com.github.boot.beans.sys.RoleInfoResponse;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.account.AccountService;
import com.github.boot.service.sys.SysMenuService;
import com.github.boot.service.sys.SysRolesMenuService;
import com.github.boot.service.sys.SysUserRolesService;
import com.github.boot.service.sys.SysUserService;
import com.github.boot.enums.sys.EnumSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRolesService sysUserRolesService;

    @Autowired
    private SysRolesMenuService sysRolesMenuService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public Object login(UserLoginParams params) {

        SysUser dbUser = validatorLoginUser(params);  // 允许"账号" 或 "手机号" 登录


        BackendTokenBean backendTokenBean = new BackendTokenBean();
        BeanUtil.copyProperties(backendTokenBean, dbUser);


        //构建用户信息
        Map<String, Object> userProfile = buildUserProfile(dbUser);

        //生成 token
        String token = UUID.randomUUID().toString().toLowerCase().replace("-","");

        return token;
    }

    private Map<String, Object> buildUserProfile(SysUser dbUser) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", dbUser.getNickName());
        result.put("photo", dbUser.getPhoto());
        result.put("account", dbUser.getAccount());
        return result;
    }

    //校验登录用户
    private SysUser validatorLoginUser(UserLoginParams params) {

        SysUser dbUser = findSysUserByAccountOrPhone(params);

        if (ObjectUtil.isNull(dbUser)) throw new PlantException("登录账号不存在~");
        if (dbUser.getState().equals(EnumSysUser.State.deleted.getKey())) throw new PlantException("您的账号已被删除,禁止登录！");
        if (dbUser.getState().equals(EnumSysUser.State.disabled.getKey())) throw new PlantException("您的账号已被禁用,无法登入系统");
        if (!dbUser.getPassword().equals(SecureUtil.sha1(params.getPassword())))
            throw new PlantException("密码错误~");

        if (dbUser.getAccountType().equals(EnumSysUser.AccountType.superAdmin.getKey())) return dbUser;

        List<SysRoles> sysRoles = sysUserRolesService.selectRolesByUserId(dbUser.getId());
        if (CollectionUtil.isEmpty(sysRoles))
            throw new PlantException("您的账号暂无权限，请联系管理员");
        return dbUser;
    }

    private SysUser findSysUserByAccountOrPhone(UserLoginParams params) {
        //1 用账号去查找用户
        SysUser account = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account", params.getUserName()));

        //2 如果账号不存在，用手机号查找用户
        if (ObjectUtil.isNull(account)) {
            account = sysUserService.getOne(new QueryWrapper<SysUser>().eq("phone", params.getUserName()));
        }
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysUser sysUser, ModifyUserPasswordParams params) {
        SysUser dbUser = sysUserService.getById(sysUser.getId());

        if (!dbUser.getPassword().equals(SecureUtil.sha1(params.getOldPwd())))
            throw new PlantException("原始密码错误~");

        dbUser.setPassword(SecureUtil.sha1(params.getNewPwd()));

        sysUserService.updateById(dbUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyPhoto(SysUser sysUser, HashMap<String, Object> params) {

        String photo = MapUtil.getStr(params, "photo");
        if (StringUtils.isEmpty(photo)) throw new PlantException("请上传头像~");

        SysUser dbUser = sysUserService.getById(sysUser.getId());
        dbUser.setPhoto(photo);

        sysUserService.updateById(dbUser);

    }

    @Override
    public Object resourceInfo(SysUser sysUser) {

        // 查询用户的角色
        List<SysRoles> sysRoles = sysUserRolesService.selectRolesByUserId(sysUser.getId());

        if (CollectionUtil.isEmpty(sysRoles)) return new ArrayList<>();

        // 获取所有的角色id
        List<Long> rolesId = sysRoles.stream().map(SysRoles::getId).collect(Collectors.toList());
        List<Long> checkMenuId = sysRoles.stream()
                .flatMap(sysRole -> Arrays.stream(sysRole.getCheckNode().split(","))
                        .map(Long::parseLong))
                .distinct()
                .collect(Collectors.toList());

        // 根据角色id 获取所有的菜单id
        List<Long> menuId = sysRolesMenuService.queryRoleMenuId(rolesId);

        // 构建菜单节点
        List<RoleInfoResponse.BasicMenu> basicMenus = sysMenuService.buildMenuNodeByMenuIds(0L, menuId, false);
        buildPermissionCode(basicMenus, checkMenuId);
        return basicMenus;
    }

    private void buildPermissionCode(List<RoleInfoResponse.BasicMenu> basicMenus, List<Long> ids) {
        basicMenus.forEach(it -> {
            if (CollectionUtil.isEmpty(it.getChild())) {
                it.setPermission(sysMenuService.buildSingleNodePermission(it.getId(), ids));
            } else {
                buildPermissionCode(it.getChild(), ids);
            }
        });
    }

}
