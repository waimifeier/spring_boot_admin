package com.github.boot.controller.account;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.github.boot.annotation.LoginUser;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.account.ModifyUserPasswordParams;
import com.github.boot.beans.request.account.UserLoginParams;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.account.AccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;


/**
 * 账号登录退出修改个人信息 控制器
 */

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;


    /**
     * 生成验证码
     *
     */

    @GetMapping("/image_code")
    public JSONReturn buildImageCode() throws IOException {
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(145, 40,4,2);
        return JSONReturn.buildSuccess(shearCaptcha.getImageBase64());
    }


    /**
     * 1.登录
     * @param params
     * @return
     */
    @RequestMapping("/login")
    public JSONReturn login(@RequestBody @Validated UserLoginParams params, @PageableDefault Pageable pageable){
        return JSONReturn.buildSuccess(accountService.login(params));
    }


    /***
     * 2.退出
     * @return
     */
    @RequestMapping("/logout")
    public JSONReturn logout(){
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3.修改密码
     * @param sysUser
     * @param params
     * @return
     */
    @RequestMapping("/modify_password")
    public JSONReturn modifyPassword(@LoginUser SysUser sysUser, @RequestBody ModifyUserPasswordParams params){
        accountService.modify(sysUser,params);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 4.修改个人头像
     * @param sysUser
     * @param params
     * @return
     */
    @RequestMapping("/modify_photo")
    public JSONReturn modifyPhoto(@LoginUser SysUser sysUser, @RequestBody HashMap<String,Object> params){
        accountService.modifyPhoto(sysUser,params);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 5.加载菜单资源
     * @param sysUser
     * @param id
     * @return
     */
    @RequestMapping("/resource_info/{id:\\d+}")
    public JSONReturn resourceInfo(@LoginUser SysUser sysUser, @PathVariable Long id){
        sysUser.setId(id);
        return JSONReturn.buildSuccess( accountService.resourceInfo(sysUser));
    }


}
