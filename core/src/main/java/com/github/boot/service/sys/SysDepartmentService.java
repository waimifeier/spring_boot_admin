package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysDepartment;

import java.util.List;

public interface SysDepartmentService extends IService<SysDepartment>{

    /**
     *  查询部门下的所有子部门id
     * @param departmentIds
     * @param currentIds
     * @return
     */
    List<Long> departmentNodeIds(List<Long> departmentIds, List<Long> currentIds);

    /**
     * 根据部门id 向上查询 返回节点树id
     * @param departmentIds
     * @param departmentId
     * @return
     */
    List<Long> queryParentNodeIdsDepartment(List<Long> departmentIds, Long departmentId);
}
