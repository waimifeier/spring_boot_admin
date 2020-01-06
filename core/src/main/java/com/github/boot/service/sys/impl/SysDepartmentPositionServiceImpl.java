package com.github.boot.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysDepartmentPositionMapper;
import com.github.boot.model.sys.SysDepartmentPosition;
import com.github.boot.service.sys.SysDepartmentPositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysDepartmentPositionServiceImpl
        extends ServiceImpl<SysDepartmentPositionMapper,SysDepartmentPosition> implements SysDepartmentPositionService {


    @Override
    public List<SysDepartmentPosition> queryPositionByDepartmentId(Long departmentId){
        return baseMapper.selectList(
                new LambdaQueryWrapper<SysDepartmentPosition>()
                        .eq(SysDepartmentPosition::getDepartmentId, departmentId)
        );
    }
}
