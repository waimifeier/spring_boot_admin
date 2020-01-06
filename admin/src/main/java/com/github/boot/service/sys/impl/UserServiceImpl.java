package com.github.boot.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.boot.beans.common.PageInfo;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.common.QueryPage;
import com.github.boot.beans.request.sys.RoleUserMappingParams;
import com.github.boot.beans.request.sys.SysUserParams;
import com.github.boot.beans.response.sys.SysUserListResponse;
import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.model.sys.*;
import com.github.boot.service.core.CoreOrganizationalService;
import com.github.boot.service.sys.*;
import com.github.boot.enums.sys.EnumDepartment;
import com.github.boot.enums.sys.EnumSysUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRolesService sysRolesService;

    @Resource
    private SysUserRolesService sysUserRolesService;

    @Resource
    private CoreOrganizationalService coreOrganizationalService;

    @Resource
    private SysDepartmentPositionService sysDepartmentPositionService;

    @Resource
    private SysDepartmentService sysDepartmentService;

    @Resource
    private SysCompanyService sysCompanyService;

    @Override
    public PageInfo userList(HashMap<String, Object> params) {

        QueryWrapper<SysUser> query = new QueryWrapper<>();
        LambdaQueryWrapper<SysUser> qw = query.lambda().orderByDesc(SysUser::getCreateTime);

        Integer type = (Integer)params.get("type"); // 1 查看全部 2：待分配，3 ： 已禁用， -1 已删除
        if(ObjectUtil.isNull(type)) throw new PlantException("类型不能为空");
        String nickName = (String) params.get("nickName");
        if(StringUtils.isNotEmpty(nickName))
            qw.and(it->it.or().likeRight(SysUser::getPhone,nickName)
                    .or().like(SysUser::getNickName,nickName)
                    .or().like(SysUser::getAccount,nickName)
            );

        switch (type){
            case 1 :  //1 查看全部
                qw.ne(SysUser::getState, EnumSysUser.State.deleted.getKey());
                break;
            case 2:
                IPage<SysUser> mapIPage = sysUserService.userNotDispatch(QueryPage.getInstance(params), nickName);
                return new PageInfo(buildSysUserList(mapIPage));
            case 3:  //已禁用
                qw.eq(SysUser::getState, EnumSysUser.State.disabled.getKey());
                break;
            case -1: //已删除
                qw.eq(SysUser::getState, EnumSysUser.State.deleted.getKey());
                break;
            default:
                throw new PlantException("类型参数错误");
        }

        IPage<SysUser> sysUserIPage = sysUserService.page(QueryPage.getInstance(params), query);
        return new PageInfo(buildSysUserList(sysUserIPage));
    }

    private IPage<SysUserListResponse> buildSysUserList(IPage<SysUser> mapIPage) {
        IPage<SysUserListResponse> sysUserIPage = new Page<>();
        sysUserIPage.setCurrent(mapIPage.getCurrent());
        sysUserIPage.setPages(mapIPage.getPages());
        sysUserIPage.setSize(mapIPage.getSize());
        sysUserIPage.setTotal(mapIPage.getTotal());

        List<SysUser> records = mapIPage.getRecords();
        List<SysUserListResponse> list = CollectionUtil.isEmpty(records)? new ArrayList<>() :
                records.stream().map(item->{
                    SysUserListResponse response = new SysUserListResponse();
                    BeanUtil.copyProperties(item,response);
                    response.setRoles(selectSysUserRoles(item.getId()));
                    response.setDepartmentArr(coreOrganizationalService.queryUserDepartmentNodeIds(item.getPositionId()));
                    buildOrganizationalData(response, item);
                    return response;
                }).collect(Collectors.toList());
        sysUserIPage.setRecords(list);

        return sysUserIPage;
    }

    /**
     *  分别查询出公司部门和职位的名称
     * @param response
     * @param item
     */
    private void buildOrganizationalData(SysUserListResponse response, SysUser item) {

        SysDepartmentPosition position = ObjectUtil.isNotNull(item.getPositionId()) ?
                    sysDepartmentPositionService.getById(item.getPositionId()) : null;
        response.setPositionName(position==null ? "" : position.getPosition());

        SysDepartment department = ObjectUtil.isNotNull(item.getDepartmentId()) ?
                sysDepartmentService.getById(item.getDepartmentId()) : null;
        response.setDepartmentName(department==null ? "" : department.getDepartmentName());

        SysCompany company = ObjectUtil.isNotNull(item.getCompanyId()) ?
                sysCompanyService.getById(item.getCompanyId()) : null;
        response.setCompanyName( company==null ? "" : company.getCompanyName() );

    }


    private List<Map<String,Object>> selectSysUserRoles(Long sysUserId) {
        List<SysRoles> sysRoles = sysUserRolesService.selectRolesByUserId(sysUserId);
        if(CollectionUtil.isEmpty(sysRoles)) return  new ArrayList<>();

        return sysRoles.stream().map(item->{
            HashMap<String,Object> data = new HashMap<>();
            data.put("id",item.getId());
            data.put("roleName",item.getRoleName());
            return data;
        }).collect(Collectors.toList());

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabledUser(Long id) {

        SysUser sysUser = sysUserService.getById(id);
        if(ObjectUtil.isNull(sysUser)) throw new PlantException("不存在该用户");
        if(sysUser.getState()==EnumSysUser.State.deleted.getKey()) throw new PlantException("该用户已删除");

        if(sysUser.getAccountType()==EnumSysUser.AccountType.superAdmin.getKey())
            throw new PlantException("禁止针对超级管理员执行此操作");

        Integer state = sysUser.getState()==EnumSysUser.State.normal.getKey() ?
                EnumSysUser.State.disabled.getKey(): EnumSysUser.State.normal.getKey();
        sysUser.setState(state);
        sysUserService.updateById(sysUser);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(Long id) {

        SysUser sysUser = sysUserService.getById(id);
        if(ObjectUtil.isNull(sysUser)) throw new PlantException("不存在该用户");
        if(sysUser.getAccountType()== EnumSysUser.AccountType.superAdmin.getKey())
            throw new PlantException("禁止删除超级管理员");

        sysUser.setState(EnumSysUser.State.deleted.getKey());
        sysUserService.updateById(sysUser);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUser sysUser, SysUserParams params) {
        SysUser sysUserParams = new SysUser();
        BeanUtil.copyProperties(params, sysUserParams);

        validatorUserAccountAndPhoneIsExists(sysUserParams.getAccount(),sysUserParams.getPhone()); //手机号 和 登录账号是否存在

        String nv = "http://pic.90sjimg.com/design/03/29/25/25/c2f81e7323b3.jpg";
        String nan = "http://pic.90sjimg.com/design/03/29/25/25/b25dfd8fc3fb.jpg";


        sysUserParams.setCreateUser(0L);
        sysUserParams.setPhoto(sysUserParams.getSex()== EnumSysUser.Sex.female.getKey()?nv:nan);
        sysUserParams.setState(EnumSysUser.State.normal.getKey());
        sysUserParams.setCreateTime(new Date());
        sysUserParams.setPassword(SecureUtil.sha1(sysUserParams.getPassword()));

        SysDepartmentPosition position = sysDepartmentPositionService.getById(sysUserParams.getPositionId());
        sysUserParams.setDepartmentId(position.getDepartmentId());
        SysDepartment department = sysDepartmentService.getById(position.getDepartmentId());
        sysUserParams.setCompanyId(department.getCompanyId());
        sysUserParams.setId(null);
        sysUserService.save(sysUserParams);

    }

    private void validatorUserAccountAndPhoneIsExists(String account,String phone) {
        validatorUserAccountIsExists(account);
        validatorUserPhoneIsExists(phone);
    }

    private void validatorUserAccountIsExists(String account){
        int accountCount = sysUserService.count(new QueryWrapper<SysUser>().eq("account", account));
        if(accountCount>0) throw new PlantException("该登录账号已存在~");
    }

    private void validatorUserPhoneIsExists(String phone){
        int phoneCount = sysUserService.count(new QueryWrapper<SysUser>().eq("phone", phone));
        if(phoneCount>0) throw new PlantException("该手机号已存在~");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUser(SysUser sysUser, SysUserParams params) {

        SysUser dbSysUser = sysUserService.getById(params.getId());

        //如果修改了登录账号，校验新输入的账号是否有人占用
        if(!dbSysUser.getAccount().equals(params.getAccount())){
            validatorUserAccountIsExists(params.getAccount());
        }
        //如果修改了手机号，校验新输入的手机号是否有人占用
        if(!dbSysUser.getPhone().equals(params.getPhone())){
            validatorUserPhoneIsExists(params.getPhone());
        }

        //如果修改了密码
        if(StringUtils.isNotEmpty(params.getPassword())){
            params.setPassword(SecureUtil.sha1(params.getPassword()));
        }else {
            params.setPassword(dbSysUser.getPassword());
        }

        SysDepartmentPosition position = sysDepartmentPositionService.getById(params.getPositionId());
        params.setDepartmentId(position.getDepartmentId());
        SysDepartment department = sysDepartmentService.getById(position.getDepartmentId());
        params.setCompanyId(department.getCompanyId());

        SysUser sysUserParams = new SysUser();
        BeanUtil.copyProperties(params, sysUserParams);

        sysUserService.updateById(sysUserParams);
    }


    @Override
    public HashMap<String,Object> badge() {
        HashMap<String,Object> data = new HashMap<>();
        List<SysUser> sysUsers = sysUserService.list(null);
        Integer all = 0, disabled = 0,remove=0;

        if(CollectionUtil.isNotEmpty(sysUsers)){
            Map<Integer, List<SysUser>> groupSysUser = sysUsers.stream()
                                        .collect(Collectors.groupingBy(SysUser::getState));
            Set<Integer> integers = groupSysUser.keySet();
            for (Integer state : integers) {
                if(state==EnumSysUser.State.deleted.getKey()) remove = groupSysUser.get(state).size();
                if(state==EnumSysUser.State.disabled.getKey()) disabled = groupSysUser.get(state).size();
                if(state==EnumSysUser.State.normal.getKey()) all = groupSysUser.get(state).size();
            }
        }
        data.put("all",all+disabled);
        data.put("disabled",disabled);
        data.put("remove",remove);
        data.put("dispatch",sysUserService.userNotDispatchCount());

        return data;
    }


    @Override
    public List<HashMap<String,Object>> roleList() {
        List<SysRoles> sysRoles = sysRolesService.allList();
        if(CollectionUtil.isEmpty(sysRoles)) return new ArrayList<>();
        return sysRoles.stream().map(item->{
            HashMap<String,Object> data = new HashMap<>();
            data.put("id",item.getId());
            data.put("name",item.getRoleName());
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleMapping(RoleUserMappingParams params) {

        //删除所有关联
        sysUserRolesService.deleteAllRoles(params.getUserId());

        //开始关联
        if(CollectionUtil.isEmpty(params.getRid())) return;

        List<SysUserRoles> rolesList = params.getRid().stream().map(rid->{
            SysUserRoles roles = new SysUserRoles();
            roles.setDeleted(false);
            roles.setSysUserId(params.getUserId());
            roles.setCreateUser(0L);
            roles.setCreateTime(new Date());
            roles.setSysRoleId(rid);
            return roles;
        }).collect(Collectors.toList());

        sysUserRolesService.saveBatch(rolesList);
    }


    @Override
    public List<DepartmentNodeResponse> department() {
        return coreOrganizationalService.buildCompanyNode(
                null,true,true, EnumDepartment.OrganizeType.POSITION);
    }
}
