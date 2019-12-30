package com.github.boot.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysRolesMapper;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.service.sys.SysRolesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 系统角色 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper, SysRoles> implements SysRolesService {


    @Override
    public List<SysRoles> allList() {
        LambdaQueryWrapper<SysRoles> q = new LambdaQueryWrapper<>();
        q.eq(SysRoles::getDeleted,false);
        List<SysRoles> sysRoles = baseMapper.selectList(q);
       return sysRoles;
    }
}
