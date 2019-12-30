package com.github.boot.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.boot.model.sys.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统账号 Mapper 接口
 * </p>
 *
 * @author dlj
 * @since 2019-08-26
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> userNotDispatch(IPage<Object> instance, @Param("nickName") String nickName);


   Integer userNotDispatchCount();
}
