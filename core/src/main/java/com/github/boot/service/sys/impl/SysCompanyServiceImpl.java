package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysCompanyMapper;
import com.github.boot.model.sys.SysCompany;
import com.github.boot.service.sys.SysCompanyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysCompanyServiceImpl extends ServiceImpl<SysCompanyMapper, SysCompany> implements SysCompanyService {



    @Override
    public List<Long> companyNodeIds(List<Long> companyIds, List<Long> currentIds){

        if(CollectionUtil.isEmpty(companyIds)) companyIds = new ArrayList<>();

        List<SysCompany> department = baseMapper.selectList(
                new LambdaQueryWrapper<SysCompany>()
                        .in(SysCompany::getParentId, currentIds)
                        .eq(SysCompany::getDeleted , false)
        );
        companyIds.addAll(currentIds);

        if(CollectionUtil.isEmpty(department)) return companyIds;

        List<Long> collect = department.stream().map(SysCompany::getId).collect(Collectors.toList());

        return companyNodeIds(companyIds, collect);
    }


    @Override
    public List<Long> queryParentNodeIdsCompany(List<Long> result, Long companyId) {
        if(CollectionUtil.isEmpty(result)) result = new ArrayList<>();


        SysCompany sysCompany = baseMapper.selectOne(
                new LambdaQueryWrapper<SysCompany>()
                        .eq(SysCompany::getId, companyId)
                        .eq(SysCompany::getDeleted , false)
        );

        if(ObjectUtil.isNull(sysCompany)) return result;

        result.add(0, sysCompany.getId());

        return  queryParentNodeIdsCompany(result, sysCompany.getParentId());
    }
}
