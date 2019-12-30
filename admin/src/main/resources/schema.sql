-- 创建数据库
CREATE DATABASE IF NOT EXISTS basic_admin default charset utf8 COLLATE utf8_general_ci;

-- use数据库
use basic_admin;

-- 创建表

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `account` varchar(32) NOT NULL COMMENT '登录账号',
  `password` varchar(40) NOT NULL COMMENT '登录密码',
  `nick_name` varchar(32) NOT NULL COMMENT '昵称',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `photo` varchar(128) DEFAULT NULL COMMENT '头像',
  `sex` int(3) DEFAULT NULL COMMENT '性别 0 女 1 男',
  `uuid` varchar(32) DEFAULT '' COMMENT 'uuid唯一值',
  `account_type` int(4) DEFAULT '1' COMMENT '账号类型 0 超级管理员 1 普通操作人员',
  `state` int(4) DEFAULT '0' COMMENT '状态：0 正常，1禁用，-1 删除',
  `subscribe` tinyint(1) DEFAULT '1' COMMENT '是否订阅消息推送 1推送 0不推送',
  `company_id` bigint(20) DEFAULT NULL COMMENT '所属公司id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '所属部门',
  `position_id` bigint(20) DEFAULT NULL COMMENT '职位id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='系统账号';

INSERT INTO `information`.`sys_user`(`id`, `create_time`, `create_user`, `account`, `password`, `nick_name`, `phone`, `login_time`, `photo`, `sex`, `uuid`, `account_type`, `state`, `subscribe`, `company_id`, `department_id`, `position_id`) VALUES (11, '2019-11-29 22:43:28', 0, 'iphone', '851aad63f2df4487f6cfebe55e4c4360a024395a', 'iphone', '13912121221', NULL, 'http://pic.90sjimg.com/design/03/29/25/25/b25dfd8fc3fb.jpg', 1, '', 1, 0, 1, 2, 1, 1);