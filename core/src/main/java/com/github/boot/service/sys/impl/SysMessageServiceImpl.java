package com.github.boot.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.dao.sys.SysMessageMapper;
import com.github.boot.model.sys.SysMessage;
import com.github.boot.service.sys.SysMessageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统消息 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    @Override
    public Object messageList(HashMap<String, Object> params) {
        //return baseMapper.selectPage();
        return null;
    }

    @Override
    public void SignRead(List<Long> ids) {

    }
}
