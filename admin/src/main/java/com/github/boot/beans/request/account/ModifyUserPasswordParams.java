package com.github.boot.beans.request.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ModifyUserPasswordParams implements Serializable{


    @NotEmpty(message = "请输入原始密码")
    private String oldPwd;

    @NotEmpty(message = "请输入新密")
    @Length(min = 6,message = "密码长度不能小于六位数")
    private String newPwd;

}
