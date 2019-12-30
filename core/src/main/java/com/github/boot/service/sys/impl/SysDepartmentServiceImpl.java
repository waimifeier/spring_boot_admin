package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysDepartmentMapper;
import com.github.boot.model.sys.SysDepartment;
import com.github.boot.service.sys.SysDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {


    /**
     * 拿到当前部门及其以下的子部门的所有id
     * @param departmentIds
     * @param currentIds
     * @return
     */
    @Override
    public List<Long> departmentNodeIds(List<Long> departmentIds, List<Long> currentIds){

        if(CollectionUtil.isEmpty(departmentIds)) departmentIds = new ArrayList<>();

        List<SysDepartment> department = baseMapper.selectList(
                new LambdaQueryWrapper<SysDepartment>()
                        .in(SysDepartment::getParentId, currentIds)
                        .eq(SysDepartment::getDeleted , false)
        );
        departmentIds.addAll(currentIds);

        if(CollectionUtil.isEmpty(department)) return departmentIds;

        List<Long> collect = department.stream().map(SysDepartment::getId).collect(Collectors.toList());

        return departmentNodeIds(departmentIds, collect);
    }


    @Override
    public List<Long> queryParentNodeIdsDepartment(List<Long> departmentIds, Long departmentId) {

        if(CollectionUtil.isEmpty(departmentIds)) departmentIds = new ArrayList<>();

        SysDepartment department = baseMapper.selectOne(
                new LambdaQueryWrapper<SysDepartment>()
                        .eq(SysDepartment::getId, departmentId)
                        .eq(SysDepartment::getDeleted , false)
        );

        if(ObjectUtil.isNull(department)) return departmentIds;

        departmentIds.add(0,department.getId());

        return queryParentNodeIdsDepartment(departmentIds, department.getParentId());
    }
}

