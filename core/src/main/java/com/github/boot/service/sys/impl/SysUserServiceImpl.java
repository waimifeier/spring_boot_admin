package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.beans.sys.BasicSysUser;
import com.github.boot.dao.sys.SysUserMapper;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.sys.SysCompanyService;
import com.github.boot.service.sys.SysDepartmentService;
import com.github.boot.service.sys.SysUserService;
import com.github.boot.enums.sys.EnumSysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统账号 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysDepartmentService sysDepartmentService;

    @Resource
    private SysCompanyService sysCompanyService;

    @Override
    public IPage<SysUser> userNotDispatch(IPage<Object> instance, String nickName) {
        return baseMapper.userNotDispatch(instance,nickName);
    }

    @Override
    public Object userNotDispatchCount() {
        return baseMapper.userNotDispatchCount();
    }


    /**
     *
     * core 公共的
     * @param companyId
     * @param departmentId
     * @return
     */
    @Override
    public List<BasicSysUser> queryUserByCompanyAndDepartment(Long companyId, Long departmentId){
        List<Long> departmentIds=null, companyIds=null ;

        if(ObjectUtil.isNotNull(companyId) && ObjectUtil.isNotNull(departmentId)){
            departmentIds = sysDepartmentService.departmentNodeIds(null, CollectionUtil.newArrayList(departmentId));
        }else {
            if (ObjectUtil.isNotNull(companyId))
                companyIds = sysCompanyService.companyNodeIds(null, CollectionUtil.newArrayList(companyId));
            if (ObjectUtil.isNotNull(departmentId))
                departmentIds =sysDepartmentService.departmentNodeIds(null, CollectionUtil.newArrayList(departmentId));
        }

        if(CollectionUtil.isEmpty(departmentIds) && CollectionUtil.isEmpty(companyIds))
            return CollectionUtil.newArrayList();

        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>()
                .ne(SysUser::getState, EnumSysUser.State.deleted.getKey());

        if(CollectionUtil.isNotEmpty(companyIds))
            query.in(SysUser::getCompanyId, companyIds);

        if(CollectionUtil.isNotEmpty(departmentIds))
            query.in(SysUser::getDepartmentId, departmentIds);

        List<SysUser> sysUsers = baseMapper.selectList(query);

        return buildBasicSysUser(sysUsers);
    }

    @Override
    public List<BasicSysUser> queryAllBasicSysUser(String nickName){

        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getState, EnumSysUser.State.normal.getKey());
        if(StringUtils.isNotEmpty(nickName)){
            query.like(SysUser::getNickName, nickName);
        }
        List<SysUser> sysUsers = baseMapper.selectList(query);

        return buildBasicSysUser(sysUsers);
    }


    private List<BasicSysUser> buildBasicSysUser(List<SysUser> sysUsers){
        return CollectionUtil.isEmpty(sysUsers) ? new ArrayList<>() :
                sysUsers.stream().map(BasicSysUser::new).collect(Collectors.toList());
    }

}
