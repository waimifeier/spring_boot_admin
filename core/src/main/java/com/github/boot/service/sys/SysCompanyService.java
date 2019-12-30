package com.github.boot.service.sys;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysCompany;

import java.util.List;

public interface SysCompanyService extends IService<SysCompany> {

    /***
     * 查询公司下的所有子公司id
     * @param companyIds
     * @param currentIds
     * @return
     */
    List<Long> companyNodeIds(List<Long> companyIds, List<Long> currentIds);

    /**
     *  根据公司id 向上查询 返回一个几点树的 ids
     * @param result
     * @param companyId
     * @return
     */
    List<Long> queryParentNodeIdsCompany(List<Long> result, Long companyId);
}
