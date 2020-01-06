package com.github.boot.controller.sys;


import com.github.boot.beans.common.JSONReturn;
import com.github.boot.service.sys.SysMessageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 *  系统消息
 */
@RestController
@RequestMapping("/sys_message")
public class SysMessageController {

    @Resource
    private SysMessageService sysMessageService;


    /**
     *  1 . 获取消息列表
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public JSONReturn messageList(@RequestBody HashMap<String,Object> params){

        return JSONReturn.buildSuccess(sysMessageService.messageList(params));
    }


    /**
     * 2. 标记为已读
     * @param ids
     * @return
     */
    @RequestMapping("/sign_read")
    public JSONReturn SignRead(@RequestBody List<Long> ids){
        sysMessageService.SignRead(ids);
        return JSONReturn.buildSuccessEmptyBody();
    }




}
