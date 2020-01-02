package com.github.boot.service.sys;



import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysCompanyDepartmentContacts;
import com.github.boot.enums.sys.EnumDepartment;

import java.util.List;

public interface SysCompanyDepartmentContactsService extends IService<SysCompanyDepartmentContacts> {
    /**
     *  根据id 和类型查询部门或公司的联系方式
     * @param id
     * @param organizeType
     * @return
     */
    List<SysCompanyDepartmentContacts> queryContacts(Long id, EnumDepartment.OrganizeType organizeType);
}
