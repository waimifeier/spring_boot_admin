package com.github.boot.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysCompanyDepartmentContactsMapper;
import com.github.boot.model.sys.SysCompanyDepartmentContacts;
import com.github.boot.service.sys.SysCompanyDepartmentContactsService;
import com.github.boot.sys.EnumDepartment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysCompanyDepartmentContactsServiceImpl extends
        ServiceImpl<SysCompanyDepartmentContactsMapper,SysCompanyDepartmentContacts> implements SysCompanyDepartmentContactsService {

    @Override
    public List<SysCompanyDepartmentContacts> queryContacts(Long id, EnumDepartment.OrganizeType organizeType) {

        List<SysCompanyDepartmentContacts> sysCompanyDepartmentContacts = baseMapper.selectList(
                new LambdaQueryWrapper<SysCompanyDepartmentContacts>()
                        .eq(SysCompanyDepartmentContacts::getDeleted, false)
                        .eq(SysCompanyDepartmentContacts::getPostId, id)
                        .eq(SysCompanyDepartmentContacts::getTypes, organizeType.getKey())
        );

        return sysCompanyDepartmentContacts;
    }
}
