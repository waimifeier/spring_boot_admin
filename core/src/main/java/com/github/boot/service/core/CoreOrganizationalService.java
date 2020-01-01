package com.github.boot.service.core;

import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.enums.sys.EnumDepartment;

import java.util.List;

/**
 * 组织架构
 */
public interface CoreOrganizationalService {

    /**
     * 构建公司节点数据 【公共的查询方法】
     * @param company 当公司为空，从根节点开始构建
     * @param departmentNode 是否需要部门节点
     * @param positionNode 是否需要职位节点
     * @param organizeType【这个字段用来判断当前节点是否禁止选择】
     * @return
     */
    List<DepartmentNodeResponse> buildCompanyNode(DepartmentNodeResponse company,
                                                  Boolean departmentNode,
                                                  Boolean positionNode,
                                                  EnumDepartment.OrganizeType organizeType);

    /**
     * 根据用户的职位id，获取所在职位的node节点id ，编辑时选择框数据回显用。
     * @param positionId
     * @return
     */
    List<Long> queryUserDepartmentNodeIds(Long positionId);
}


