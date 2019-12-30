package com.github.boot.sys;

public class EnumSysMenu {

    /**
     * 菜单类型
    * */
    public enum MenuType{

        ALL("all","所有"),
        MENU("MENU" , "菜单"),
        BUTTON("BUTTON" , "按钮"),
        MODULE("MODULE" , "首页资源模块")
        ;

        private String key;
        private String val;

        MenuType() {
        }

        MenuType(String key, String val) {
            this.key = key;
            this.val = val;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
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
