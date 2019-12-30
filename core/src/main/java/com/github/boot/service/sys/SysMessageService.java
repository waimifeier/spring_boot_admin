package com.github.boot.service.sys;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysMessage;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统消息 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysMessageService extends IService<SysMessage> {

    Object messageList(HashMap<String, Object> params);

    void SignRead(List<Long> ids);
}
