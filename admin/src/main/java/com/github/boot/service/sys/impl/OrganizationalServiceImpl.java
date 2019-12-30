package com.github.boot.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.request.sys.DepartmentRequestParams;
import com.github.boot.beans.response.sys.DepartmentDetailResponse;
import com.github.boot.beans.sys.BasicSysUser;
import com.github.boot.beans.sys.DepartmentNodeResponse;
import com.github.boot.beans.sys.TreeNodeResponse;
import com.github.boot.model.sys.*;
import com.github.boot.service.core.CoreOrganizationalService;
import com.github.boot.service.core.CoreTreeNodeService;
import com.github.boot.service.sys.*;
import com.github.boot.sys.EnumDepartment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationalServiceImpl implements OrganizationalService {


    @Resource
    private SysCompanyService companyService;

    @Resource
    private SysCompanyDepartmentContactsService sysCompanyDepartmentContactsService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private CoreOrganizationalService coreOrganizationalService;

    @Resource
    private CoreTreeNodeService coreTreeNodeService;

    @Resource
    private SysDepartmentPositionService sysDepartmentPositionService;

    @Resource
    private SysDepartmentService departmentService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDepartment(DepartmentRequestParams params, Long userId) {

        String code = params.getCode();
        checkParams(code); // 校验code是否是可选值

        String parentCode = params.getParentCode();
        checkParams(parentCode); // 校验code是否是可选值

        if(parentCode.equals(EnumDepartment.OrganizeType.DEPARTMENT.getKey())){
            checkDepartment(params, userId, code);
        }
        else if(parentCode.equals(EnumDepartment.OrganizeType.COMPANY.getKey())){
            checkCompany(params, userId, code);
        }
    }

    private void checkCompany(DepartmentRequestParams params, Long userId, String code) {
        SysCompany dbCompany = companyService.getById(params.getParentId());
        if(ObjectUtil.isNull(dbCompany) || dbCompany.getDeleted())
            throw new PlantException("上级公司不存在");
        if(code.equals(EnumDepartment.OrganizeType.COMPANY.getKey())){
            SysCompany company = new SysCompany();
            company.setCreateUser(userId);
            company.setParentId(dbCompany.getId());
            saveOrUpdateCompany(params, company);
        }

        else if(code.equals(EnumDepartment.OrganizeType.DEPARTMENT.getKey())){
            SysDepartment department = new SysDepartment();
            department.setCompanyId(dbCompany.getId());
            department.setParentId(0L);
            department.setCreateUser(userId);
            saveOrUpdateDepartment(params, department);
        }
    }

    private void checkDepartment(DepartmentRequestParams params, Long userId, String code) {
        SysDepartment dbDepartment = departmentService.getById(params.getParentId());
        if(ObjectUtil.isNull(dbDepartment) || dbDepartment.getDeleted())
            throw new PlantException("上级部门不存在");
        if(code.equals(EnumDepartment.OrganizeType.COMPANY.getKey()))
            throw new PlantException("部门下不允许添加公司");

        SysDepartment department = new SysDepartment();
        department.setCompanyId(dbDepartment.getCompanyId());
        department.setParentId(dbDepartment.getId());
        department.setCreateUser(userId);
        saveOrUpdateDepartment(params, department);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDepartment(DepartmentRequestParams params, Long userId) {

        String code = params.getCode();
        checkParams(code); // 校验code是否是可选值

        if (code.equals(EnumDepartment.OrganizeType.COMPANY.getKey())) {
            SysCompany dbCompany = companyService.getById(params.getId());
            if(ObjectUtil.isNull(dbCompany) || dbCompany.getDeleted())
                throw new PlantException("您修改的公司不存在");
            saveOrUpdateCompany(params, dbCompany);
        }

        else if(code.equals(EnumDepartment.OrganizeType.DEPARTMENT.getKey())) {
            SysDepartment dbDepartment = departmentService.getById(params.getId());
            if(ObjectUtil.isNull(dbDepartment) || dbDepartment.getDeleted())
                throw new PlantException("您修改的部门不存在");
            saveOrUpdateDepartment(params, dbDepartment);
        }
    }

    @Override
    public List<TreeNodeResponse> agentList() {
        List<BasicSysUser> basicSysUsers = sysUserService.queryAllBasicSysUser(null);
        return CollectionUtil.isEmpty(basicSysUsers)? new ArrayList<>():
                basicSysUsers.stream().map(
                        it-> coreTreeNodeService.buildTreeNode(it.getId(),it.getNickName(),queryUserPosition(it.getPositionId()))
                ).collect(Collectors.toList());
    }

    private String queryUserPosition(Long positionId) {
        SysDepartmentPosition position = sysDepartmentPositionService.getById(positionId);
        return Optional.ofNullable(position).map(SysDepartmentPosition::getPosition).orElse("");
    }

    @Override
    public List<DepartmentNodeResponse> companyOrDepartmentList(String code) {
        Boolean isDepartment  = EnumDepartment.OrganizeType.DEPARTMENT.getKey().equals(code);

        EnumDepartment.OrganizeType department =
                isDepartment ? EnumDepartment.OrganizeType.DEPARTMENT : EnumDepartment.OrganizeType.COMPANY;

        return coreOrganizationalService.buildCompanyNode(null,isDepartment, false , department);
    }



    @Override
    public List<DepartmentNodeResponse> nodeList() {
        return coreOrganizationalService.buildCompanyNode(
                null,
                true,
                false,
                EnumDepartment.OrganizeType.COMPANY
        );
    }

    @Override
    public Map<String, Object> detail(DepartmentRequestParams params) {
        Long id = params.getId();
        String code = params.getCode();

        if(ObjectUtil.isNull(id) || StringUtils.isEmpty(code)) throw new PlantException("参数异常");
        checkParams(code);   //检查code是否在可选值范围内

        Map<String, Object> result = new HashMap<>();
        result.put("currentNode", null);
        result.put("parentNode", null);

        if(code.equals(EnumDepartment.OrganizeType.COMPANY.getKey()))
            return getCompanyDetail(id, result);
        else return getDepartmentDetail(id, result);

    }


    private void checkParams(String code){

        List<String> codes = Arrays.asList(
                EnumDepartment.OrganizeType.COMPANY.getKey(),
                EnumDepartment.OrganizeType.DEPARTMENT.getKey()
        );

        if(!codes.contains(code)){
            throw new PlantException("code参数错误");
        }
    }

    private void saveOrUpdateCompany(DepartmentRequestParams params, SysCompany company) {
        company.setAddress(params.getAddress());
        company.setCompanyName(params.getName());
        company.setAgentId(params.getAgentId());

        if(ObjectUtil.isNull(company.getId())){
            company.setCreateTime(new Date());
            company.setDeleted(false);
            companyService.save(company);
            // 保存或修改联系方式
            saveOrUpdateContacts(params, company.getCreateUser(),EnumDepartment.OrganizeType.COMPANY, company.getId());
        }else {
            companyService.updateById(company);
            // 保存或修改联系方式
            saveOrUpdateContacts(params, company.getCreateUser(),EnumDepartment.OrganizeType.COMPANY, company.getId());
        }
    }

    private void saveOrUpdateDepartment (DepartmentRequestParams params, SysDepartment dbDepartment){
        dbDepartment.setDepartmentName(params.getName());
        dbDepartment.setAgentUser(params.getAgentId());
        if(dbDepartment.getId()==null){
            dbDepartment.setDeleted(false)
                    .setCreateTime(new Date());
            departmentService.save(dbDepartment);

            // 保存联系方式
            saveOrUpdateContacts(
                    params,
                    dbDepartment.getCreateUser(),
                    EnumDepartment.OrganizeType.DEPARTMENT,
                    dbDepartment.getId()
            );
            // 保存职位信息
            saveOrUpdatePosition(params, dbDepartment.getCreateUser(), dbDepartment.getId());
        }else {
            departmentService.updateById(dbDepartment);

            // 修改联系方式
            saveOrUpdateContacts(params, dbDepartment.getCreateUser(),EnumDepartment.OrganizeType.DEPARTMENT, dbDepartment.getId());
            // 修改职位信息
            saveOrUpdatePosition(params, dbDepartment.getCreateUser(), dbDepartment.getId());
        }
    }

    /**
     * 保存或更新联系方式
     * @param params
     * @param userId
     * @param organizeType
     * @param postId
     */
    private void saveOrUpdateContacts( DepartmentRequestParams params, Long userId,
                                       EnumDepartment.OrganizeType organizeType ,Long postId){

        List<DepartmentRequestParams.Contacts> contactsParams = params.getContacts();

        LambdaQueryWrapper<SysCompanyDepartmentContacts> query = new LambdaQueryWrapper<SysCompanyDepartmentContacts>()
                .eq(SysCompanyDepartmentContacts::getPostId, postId)
                .eq(SysCompanyDepartmentContacts::getTypes, organizeType.getKey())
                .eq(SysCompanyDepartmentContacts::getDeleted, false);

        List<SysCompanyDepartmentContacts> dbCompanyDepartmentContacts = sysCompanyDepartmentContactsService.list(query);

        // 如果前端传入的联系方式为空, 删除数据库的联系方式
        if(CollectionUtil.isEmpty(contactsParams)){

            if( CollectionUtil.isEmpty(dbCompanyDepartmentContacts) ) return;

            SysCompanyDepartmentContacts modify = new SysCompanyDepartmentContacts();
            modify.setDeleted(true);
            sysCompanyDepartmentContactsService.update(modify, query);   // 删除所有
            return;
        }

        // 记录数据库原来保存的联系方式
        List<Long> dbContactsIds = CollectionUtil.isEmpty(dbCompanyDepartmentContacts) ? new ArrayList<>() :
                dbCompanyDepartmentContacts.stream().map(SysCompanyDepartmentContacts::getId).collect(Collectors.toList());

        List<Long> paramsId = new ArrayList<>();
        for (DepartmentRequestParams.Contacts contactsParam : contactsParams) {
            SysCompanyDepartmentContacts update = new SysCompanyDepartmentContacts();
            BeanUtil.copyProperties(contactsParam ,update);

            if(ObjectUtil.isNull(contactsParam.getId()) || contactsParam.getId()<=0){
                update.setCreateTime(new Date());
                update.setDeleted(false);
                update.setTypes(organizeType.getKey());
                update.setCreateUser(userId);
                update.setPostId(postId);
                sysCompanyDepartmentContactsService.save(update);
                continue;
            }

            if(dbContactsIds.contains(contactsParam.getId())){
                sysCompanyDepartmentContactsService.updateById(update);
                paramsId.add(contactsParam.getId());
            }
        }

        // 与数据库对比，处理前端用户删除的数据
        dbContactsIds.removeAll(paramsId);
        if(CollectionUtil.isEmpty(dbContactsIds)) return;
        SysCompanyDepartmentContacts remove = new SysCompanyDepartmentContacts();
        remove.setDeleted(true);
        LambdaQueryWrapper removeQuery = new LambdaQueryWrapper<SysCompanyDepartmentContacts>()
                .in(SysCompanyDepartmentContacts::getId, dbContactsIds);
        sysCompanyDepartmentContactsService.update(remove, removeQuery);   // 删除用户删除的数据
    }

    /**
     * 保存或更新部门职位
     * @param params
     * @param userId
     * @param dbDepartmentId
     */
    private void saveOrUpdatePosition(DepartmentRequestParams params,Long userId, Long dbDepartmentId){

        List<DepartmentRequestParams.Position> positions = params.getPositions();

        // 1. 构建查询条件
        LambdaQueryWrapper<SysDepartmentPosition> query = new LambdaQueryWrapper<SysDepartmentPosition>()
                .eq(SysDepartmentPosition::getDeleted, false)
                .eq(SysDepartmentPosition::getDepartmentId, dbDepartmentId);

        // 2.查询数据库该部门的联系人
        List<SysDepartmentPosition> sysDepartmentPositions = sysDepartmentPositionService.list(query);

        // 3.如果前端传入的为空，将数据库所有的职位删除
        if (CollectionUtil.isEmpty(positions)) {
            if(CollectionUtil.isEmpty(sysDepartmentPositions)) return;
            SysDepartmentPosition delete =  new SysDepartmentPosition();
            delete.setDeleted(true);
            sysDepartmentPositionService.update(delete,query);
            return;
        }

        // 4. 拿到该部门的所有职位id
        List<Long> dbPositionIds = CollectionUtil.isEmpty(sysDepartmentPositions) ? new ArrayList<>() :
                sysDepartmentPositions.stream().map(SysDepartmentPosition::getId).collect(Collectors.toList());

        // 5. 如果前端传入职位不为空，开始循环
        List<Long> paramsId = new ArrayList<>();
        for (DepartmentRequestParams.Position position : positions) {
            //5.1 如果职位id为空k,保存当前这个在职位，保存完成后，跳过当前循环，继续下一次循环
            if(ObjectUtil.isNull(position.getId()) || position.getId()<=0){
                SysDepartmentPosition sp = new SysDepartmentPosition();
                sp.setCreateTime(new Date());
                sp.setDeleted(false);
                sp.setDepartmentId(dbDepartmentId);
                sp.setPosition(position.getPositionName());
                sp.setCreateUser(userId);
                sysDepartmentPositionService.save(sp);
                continue;
            }
            // 如果不为空，修改当前这条职位信息,并将这条修改数据的id存下来（判断是否有删除记录会用到）
            if(dbPositionIds.contains(position.getId())){
                SysDepartmentPosition sp = new SysDepartmentPosition();
                sp.setId(position.getId());
                sp.setPosition(position.getPositionName());
                sysDepartmentPositionService.updateById(sp);
                paramsId.add(position.getId());
            }
        }

        // 6. 与数据库对比，处理前端用户删除的数据
        dbPositionIds.removeAll(paramsId);
        if(CollectionUtil.isEmpty(dbPositionIds)) return;
        // 7. 取交集后，剩下的数据，就是前端本次删除的职位信息。
        SysDepartmentPosition remove = new SysDepartmentPosition();
        remove.setDeleted(true);
        LambdaQueryWrapper removeQuery = new LambdaQueryWrapper<SysDepartmentPosition>()
                .in(SysDepartmentPosition::getId,dbPositionIds);
        sysDepartmentPositionService.update(remove, removeQuery);
    }


    private Map<String, Object> getDepartmentDetail(Long id, Map<String, Object> result) {
        SysDepartment dbCurrentDepartment = departmentService.getById(id);
        if(ObjectUtil.isNull(dbCurrentDepartment) || dbCurrentDepartment.getDeleted())
            throw new PlantException("您查看您的数据不存在");

        DepartmentDetailResponse currentNode = buildDepartmentDetail(dbCurrentDepartment);
        result.put("currentNode", currentNode);
        result.put("currentNodeUser",sysUserService.queryUserByCompanyAndDepartment(
                null,dbCurrentDepartment.getId()));

        Long companyId = dbCurrentDepartment.getCompanyId();
        SysCompany parentCompany = companyService.getById(companyId);
        if(ObjectUtil.isNull(parentCompany))  return result;

        DepartmentDetailResponse parentNode = buildCompanyDetail(parentCompany);
        result.put("parentNode", parentNode);
        return result;
    }




    private Map<String, Object> getCompanyDetail(Long id, Map<String, Object> result) {
        SysCompany dbCurrentCompany = companyService.getById(id);
        if(ObjectUtil.isNull(dbCurrentCompany)) throw new PlantException("您查看您的数据不存在");

        DepartmentDetailResponse currentNode = buildCompanyDetail(dbCurrentCompany);
        result.put("currentNode", currentNode);
        result.put("currentNodeUser", sysUserService.queryUserByCompanyAndDepartment(
                dbCurrentCompany.getId(),null));

        Long parentId = dbCurrentCompany.getParentId();
        if( parentId <= 0 ) return result;

        SysCompany dbParentCompany = companyService.getById(parentId);
        if(ObjectUtil.isNull(dbParentCompany) || dbParentCompany.getDeleted()) return result;

        DepartmentDetailResponse parentNode = buildCompanyDetail(dbParentCompany);
        result.put("parentNode", parentNode);
        return result;
    }

    private DepartmentDetailResponse buildDepartmentDetail( SysDepartment department){
        DepartmentDetailResponse currentNode = new DepartmentDetailResponse();
        currentNode.setAddress("");
        currentNode.setAgentId(department.getAgentUser());
        SysUser sysUser = sysUserService.getById(department.getAgentUser());
        currentNode.setAgentUser(ObjectUtil.isNull(sysUser) ? "代理人未知" : sysUser.getNickName());
        currentNode.setCode(EnumDepartment.OrganizeType.DEPARTMENT.getKey());
        currentNode.setName(department.getDepartmentName());
        currentNode.setId(department.getId());
        currentNode.setContacts( buildContacts(department.getId(), EnumDepartment.OrganizeType.DEPARTMENT));
        currentNode.setPositions(buildPosition(department.getId()));
        return currentNode;
    }

    private DepartmentDetailResponse buildCompanyDetail( SysCompany sysCompany){
        DepartmentDetailResponse currentNode = new DepartmentDetailResponse();
        currentNode.setAddress(sysCompany.getAddress());
        currentNode.setAgentId(sysCompany.getAgentId());
        SysUser sysUser = sysUserService.getById(sysCompany.getAgentId());
        currentNode.setAgentUser(ObjectUtil.isNull(sysUser) ? "代理人未知" : sysUser.getNickName());
        currentNode.setCode(EnumDepartment.OrganizeType.COMPANY.getKey());
        currentNode.setName(sysCompany.getCompanyName());
        currentNode.setId(sysCompany.getId());
        currentNode.setContacts( buildContacts(sysCompany.getId(), EnumDepartment.OrganizeType.COMPANY));
        return currentNode;
    }

    /**
     * 根据部门获取职位
     * @param departmentId
     * @return
     */
    private List<DepartmentDetailResponse.Position> buildPosition(Long departmentId) {

        List<SysDepartmentPosition> sysDepartmentPositions = sysDepartmentPositionService.queryPositionByDepartmentId(departmentId);

        return CollectionUtil.isEmpty(sysDepartmentPositions) ? new ArrayList<>():
                sysDepartmentPositions.stream().map(item ->{
                    DepartmentDetailResponse.Position position = new DepartmentDetailResponse.Position();
                    position.setId(item.getId());
                    position.setPositionName(item.getPosition());
                    return position;
                }).collect(Collectors.toList());
    }

    /**
     * 查询公司或部门的联系人
     * @param id
     * @param organizeType
     * @return
     */
    private List<DepartmentDetailResponse.Contacts> buildContacts(Long id,EnumDepartment.OrganizeType organizeType) {
        List<SysCompanyDepartmentContacts> sysCompanyDepartmentContacts = sysCompanyDepartmentContactsService.queryContacts(id, organizeType);

        return CollectionUtil.isEmpty(sysCompanyDepartmentContacts) ?  new ArrayList<>() :
                sysCompanyDepartmentContacts.stream().map(item ->{
                    DepartmentDetailResponse.Contacts contacts = new DepartmentDetailResponse.Contacts();
                    contacts.setContactNumber(item.getContactNumber());
                    contacts.setContacts(item.getContacts());
                    contacts.setId(item.getId());
                    return  contacts;
                }).collect(Collectors.toList());
    }


}
