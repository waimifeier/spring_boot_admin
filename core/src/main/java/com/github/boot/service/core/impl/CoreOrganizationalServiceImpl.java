package com.github.boot.service.core.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.beans.sys.TreeNodeResponse;
import com.github.boot.model.sys.SysCompany;
import com.github.boot.model.sys.SysDepartment;
import com.github.boot.model.sys.SysDepartmentPosition;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.core.CoreOrganizationalService;
import com.github.boot.service.core.CoreTreeNodeService;
import com.github.boot.service.sys.SysCompanyService;
import com.github.boot.service.sys.SysDepartmentPositionService;
import com.github.boot.service.sys.SysDepartmentService;
import com.github.boot.service.sys.SysUserService;
import com.github.boot.enums.sys.EnumDepartment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Transactional(readOnly = true)
@Service
public class CoreOrganizationalServiceImpl implements CoreOrganizationalService {

    @Resource
    private SysCompanyService sysCompanyService;

    @Resource
    private CoreTreeNodeService coreTreeNodeService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysDepartmentPositionService sysDepartmentPositionService;

    @Resource
    private SysDepartmentService sysDepartmentService ;

    /**
     * 构建公司节点数据 【公共的查询方法】
     * @param company 当公司为空，从根节点开始构建
     * @param departmentNode 是否需要部门节点
     * @param positionNode 是否需要职位节点
     * @param organizeType【这个字段用来判断当前节点是否禁止选择】
     * @return
     */
    @Override
    public List<DepartmentNodeResponse> buildCompanyNode(DepartmentNodeResponse company,
                                                          Boolean departmentNode,
                                                          Boolean positionNode,
                                                          EnumDepartment.OrganizeType organizeType) {

        List<DepartmentNodeResponse> result = new ArrayList<>();
        List<SysCompany> dbCompanies = sysCompanyService.list(
                new LambdaQueryWrapper<SysCompany>()
                        .eq(SysCompany::getParentId, ObjectUtil.isNotNull(company) ? company.getValue() : 0L)
        );

        // 如果传入公司没有子公司
        if (CollectionUtil.isEmpty(dbCompanies)){
            // 如果公司为空返回空，如果公司不为空查询这个公司所有部门节点
            return ObjectUtil.isNotNull(company) && departmentNode
                    ? buildDepartmentNode(company.getValue(), new DepartmentNodeResponse(company.getPath()),positionNode,organizeType): null;
        }

        for (SysCompany _company : dbCompanies) {
            TreeNodeResponse treeNodeResponse = coreTreeNodeService.buildTreeNode(
                    _company.getId(),
                    _company.getCompanyName(),
                    EnumDepartment.OrganizeType.COMPANY.getKey()
            );
            DepartmentNodeResponse response = new DepartmentNodeResponse();
            BeanUtil.copyProperties(treeNodeResponse, response);

            response.setPath(buildCurrentPaths(ObjectUtil.isNull(company)? null:company.getPath() ,response));
            List<DepartmentNodeResponse> departmentNodeResponses = buildCompanyNode(response, departmentNode, positionNode, organizeType);
            // 查询当前节点的子节点是公司还是部门
            List<String> addCodes = childCompanyNodeIsWhat(_company.getId());
            String addCode = addCodes.get(0);
            response.setAddCode(addCode);
            response.setDisabled(!addCodes.contains(organizeType.getKey()));
            response.setChildren(departmentNodeResponses);
            result.add(response);
        }

        return result;
    }



    /**
     * 构建部门节点数据
     * @param companyId 所属公司id
     * @param department 当部门为空，从根节点开始构建
     * @return
     */
    private List<DepartmentNodeResponse> buildDepartmentNode(Long companyId,
                                                             DepartmentNodeResponse department,
                                                             Boolean positionNode,
                                                             EnumDepartment.OrganizeType organizeType) {

        List<DepartmentNodeResponse> result = new ArrayList<>();
        List<SysDepartment> dbDepartment = sysDepartmentService.list(
                new LambdaQueryWrapper<SysDepartment>()
                        .eq(SysDepartment::getCompanyId, companyId)
                        .eq(SysDepartment::getParentId, ObjectUtil.isNull(department.getValue())? 0L : department.getValue())
        );

        if (CollectionUtil.isEmpty(dbDepartment)) {
            // 因为上面公司将path带入进来，所有公司下的一级部门不可能为空， 可以他的value判断
            return ObjectUtil.isNotNull(department.getValue()) && positionNode ? buildPositionNode(department.getValue()) : null;
        }

        for (SysDepartment _department : dbDepartment) {
            TreeNodeResponse treeNodeResponse = coreTreeNodeService.buildTreeNode(
                    _department.getId(),
                    _department.getDepartmentName(),
                    EnumDepartment.OrganizeType.DEPARTMENT.getKey()
            );
            DepartmentNodeResponse response = new DepartmentNodeResponse();
            BeanUtil.copyProperties(treeNodeResponse, response);

            response.setPath(buildCurrentPaths(department.getPath() ,response));
            List<DepartmentNodeResponse> departmentNodeResponses = buildDepartmentNode(_department.getCompanyId(), response, positionNode ,organizeType);
            String addCode = childDepartmentNodeIsWhat(_department.getId());
            response.setAddCode(addCode);
            if(organizeType.getKey().equals(EnumDepartment.OrganizeType.POSITION.getKey()))
                response.setDisabled(true);
            else
                response.setDisabled(!addCode.equals(organizeType.getKey()));
            response.setChildren(departmentNodeResponses);
            result.add(response);
        }

        return result;
    }


    List<Long> buildCurrentPaths(List<Long> historyPath,DepartmentNodeResponse response){
        List<Long> path = new ArrayList<>();
        path.add(response.getValue());

        if (CollectionUtil.isNotEmpty(historyPath)) {
            path.addAll(0,historyPath);
        }
        return path;
    }


    private List<DepartmentNodeResponse> buildPositionNode(Long departmentId) {

        List<SysDepartmentPosition> dbDepartmentPositions = sysDepartmentPositionService.queryPositionByDepartmentId(departmentId);

        if(CollectionUtil.isEmpty(dbDepartmentPositions)) return null;

        return dbDepartmentPositions.stream().map(item ->{
            TreeNodeResponse treeNodeResponse = coreTreeNodeService.buildTreeNode(
                    item.getId(),
                    item.getPosition(),
                    EnumDepartment.OrganizeType.POSITION.getKey()
            );
            DepartmentNodeResponse response = new DepartmentNodeResponse();
            BeanUtil.copyProperties(treeNodeResponse,response);
            return response;
        }).collect(Collectors.toList());

    }

    private String childDepartmentNodeIsWhat(Long departmentId){

        Integer childDeptCount = sysDepartmentService.count(
                new LambdaQueryWrapper<SysDepartment>()
                        .eq(SysDepartment::getParentId, departmentId)
        );

        if(childDeptCount>0) return EnumDepartment.OrganizeType.DEPARTMENT.getKey();

        Integer useDepartmentCount = sysUserService.count(
                new LambdaQueryWrapper<SysUser>()
                        .in(SysUser::getDepartmentId, sysDepartmentService.departmentNodeIds(
                                null, CollectionUtil.newArrayList(departmentId))
                        )
        );

        if(useDepartmentCount > 0 ) return EnumDepartment.OrganizeType.POSITION.getKey();
        return EnumDepartment.OrganizeType.DEPARTMENT.getKey();
    }

    /**
     * 当前公司的下级节点什么
     * @param companyId
     * @return
     */
    private List<String> childCompanyNodeIsWhat(Long companyId){

        Integer childCompanyCount = sysCompanyService.count(
                new LambdaQueryWrapper<SysCompany>()
                        .eq(SysCompany::getParentId, companyId)
        );

        if( childCompanyCount > 0 ){
            return Collections.singletonList(EnumDepartment.OrganizeType.COMPANY.getKey());
        }

        Integer childDeptCount = sysDepartmentService.count(
                new LambdaQueryWrapper<SysDepartment>()
                        .eq(SysDepartment::getCompanyId, companyId)
        );

        if(childDeptCount<=0) {
            return asList(
                    EnumDepartment.OrganizeType.COMPANY.getKey(),
                    EnumDepartment.OrganizeType.DEPARTMENT.getKey()
            );
        }
        return Collections.singletonList(EnumDepartment.OrganizeType.DEPARTMENT.getKey());
    }


    @Override
    public List<Long> queryUserDepartmentNodeIds(Long positionId) {
        List<Long> result = new ArrayList<>();
        SysDepartmentPosition position = sysDepartmentPositionService.getById(positionId);
        if(ObjectUtil.isNull(position)) return result;
        result.add(0,positionId);

        List<Long> departmentIds = sysDepartmentService.queryParentNodeIdsDepartment(null, position.getDepartmentId());

        if(CollectionUtil.isEmpty(departmentIds)) return result;

        result.addAll(0,departmentIds);

        // 第一个存的第一级部门的id
        Long firstDepartmentId = departmentIds.get(0);
        SysDepartment firstDepartment = sysDepartmentService.getById(firstDepartmentId);
        if(ObjectUtil.isNull(firstDepartment)) return result;

        List<Long> companyIds = sysCompanyService.queryParentNodeIdsCompany(null, firstDepartment.getCompanyId());
        if(CollectionUtil.isEmpty(companyIds)) return result;

        result.addAll(0,companyIds);

        return result;
    }
}
