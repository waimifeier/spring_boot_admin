package com.github.boot.service.sys;


import com.github.boot.beans.request.sys.DepartmentRequestParams;
import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.beans.sys.TreeNodeResponse;

import java.util.List;
import java.util.Map;

public interface OrganizationalService {

    /**
     * 添加公司或部门
     * @param params
     */
    void addDepartment(DepartmentRequestParams params, Long userId);

    /**
     *  修改公司或部门
     * @param params
     */
    void modifyDepartment(DepartmentRequestParams params, Long userId);


    /**
     * 构建组织架构节点数
     * @return
     */
    List<DepartmentNodeResponse> nodeList();

    /**
     * 查看详情
     * @param params
     * @return
     */
    Map<String, Object> detail(DepartmentRequestParams params);

    List<TreeNodeResponse> agentList();

    /**
     * 获取公司或部门节点
     * @return
     */
    List<DepartmentNodeResponse> companyOrDepartmentList(String code);
}
