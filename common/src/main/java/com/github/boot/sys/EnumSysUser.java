package com.github.boot.sys;


// 系统用户枚举
public class EnumSysUser {


    public enum  AccountType{
        superAdmin(0,"超级管理员"),
        admin(1,"普通用户"),
        ;

        private Integer key;
        private String val;

        AccountType(){}

        AccountType(Integer key, String val) {
            this.key = key;
            this.val = val;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    public enum  Sex{
        male(1,"男"),
        female(0,"女"),
        ;

        private Integer key;
        private String val;

        Sex(){}

        Sex(Integer key, String val) {
            this.key = key;
            this.val = val;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }


    public enum  State{
        normal(0,"正常"),
        disabled(1,"禁用"),
        deleted(-1,"删除")
        ;

        private Integer key;
        private String val;

        State() {
        }

        State(Integer key, String val) {
            this.key = key;
            this.val = val;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
