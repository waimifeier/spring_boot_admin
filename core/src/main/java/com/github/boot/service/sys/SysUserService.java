package com.github.boot.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.beans.sys.BasicSysUser;
import com.github.boot.model.sys.SysUser;

import java.util.List;


/**
 * <p>
 * 系统账号 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysUserService extends IService<SysUser>{
    

    IPage<SysUser> userNotDispatch(IPage<Object> instance, String nickName);

    Object userNotDispatchCount();

    /**
     *  查询所有的系统用户
     * @return
     */
    List<BasicSysUser> queryAllBasicSysUser(String nickName);


    /***
     * 根据部门或公司查询有那些用户
     * @param companyId  两个参数 2 选 1
     * @param departmentId
     * @return
     */
    List<BasicSysUser> queryUserByCompanyAndDepartment(Long companyId, Long departmentId);
}
