package com.github.boot.beans.sys;

import com.github.boot.model.sys.SysUser;
import lombok.Data;

@Data
public class BasicSysUser{
        private Long id;
        private String nickName;
        private String account;
        private String phone;
        private String photo;
        private Long positionId;


        public BasicSysUser(SysUser sysUser) {
                this.id = sysUser.getId();
                this.nickName = sysUser.getNickName();
                this.account = sysUser.getAccount();
                this.phone = sysUser.getPhone();
                this.photo = sysUser.getPhoto();
                this.positionId = sysUser.getPositionId();
        }

        public BasicSysUser(){

        }
}