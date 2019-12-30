package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysDepartmentPosition;

import java.util.List;

public interface SysDepartmentPositionService extends IService<SysDepartmentPosition> {

    /**
     *  根据部门id查询关联的职位
     * @param departmentId
     * @return
     */
    List<SysDepartmentPosition> queryPositionByDepartmentId(Long departmentId);
}
