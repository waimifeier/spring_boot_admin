package com.github.boot.enums.sys;

public class EnumDepartment {

    public enum OrganizeType{
        COMPANY("COMPANY" , "公司"),
        DEPARTMENT("DEPARTMENT" ,"部门"),
        POSITION("POSITION" ,"职位"),
        ;

        private String key;
        private String val;

        OrganizeType() {
        }

        OrganizeType(String key, String val) {
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
