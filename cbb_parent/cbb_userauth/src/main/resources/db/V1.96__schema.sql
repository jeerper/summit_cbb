CREATE TABLE IF NOT EXISTS `sys_user_auth`(
  `id` varchar(40) NOT NULL COMMENT '主键',
  `userName_auth` varchar(50) NOT NULL COMMENT '用户登录名',
  `name_auth` varchar(50)  DEFAULT NULL COMMENT '用户姓名',
  `sex_auth` varchar(10)  DEFAULT NULL COMMENT '性别(1男 2女)',
  `password_auth` varchar(255)  DEFAULT NULL COMMENT '密码',
  `email_auth` varchar(100)  DEFAULT NULL COMMENT '邮箱',
  `phone_number_auth` varchar(100) DEFAULT NULL COMMENT '电话号码',
  `is_enabled_auth` varchar(10)  DEFAULT NULL COMMENT '是否启用 0禁用1启用',
  `headPortrait_auth` varchar(50)  DEFAULT NULL COMMENT '头像',
  `duty_auth` varchar(50)  DEFAULT NULL COMMENT '岗位',
  `dept_auth` varchar(50)  DEFAULT NULL COMMENT '部门',
  `adcd_auth` varchar(50)  DEFAULT NULL COMMENT '行政区划',
  `post_auth` varchar(50)  DEFAULT NULL COMMENT '职位',
  `auth_person` varchar(50)  DEFAULT NULL COMMENT '审核人',
  `isAudited` varchar(10)  DEFAULT NULL COMMENT '审核是否通过(0：发起审核，1：通过，2：不通过)',
  `auth_time` datetime  DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;



CREATE TABLE IF NOT EXISTS `sys_dept_auth` (
  `id` varchar(40) NOT NULL COMMENT '主键id',
  `deptId_auth` varchar(40) NOT NULL COMMENT '部门id',
  `pId_auth` varchar(40) DEFAULT NULL COMMENT '上级部门id',
  `deptcode_auth` varchar(20) DEFAULT NULL COMMENT '部门编号',
  `deptName_auth` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `adcd_auth` varchar(20) DEFAULT NULL COMMENT '行政区划',
  `auth_person` varchar(50)  DEFAULT NULL COMMENT '审核人',
  `isAudited` varchar(10)  DEFAULT NULL COMMENT '审核是否通过(0：发起审核，1：通过，2：不通过)',
  `auth_time` datetime  DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
