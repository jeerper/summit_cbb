CREATE TABLE IF NOT EXISTS `sys_user_record`(
  `username` varchar(40) NOT NULL COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '用户登录名',
  `sex` varchar(10)  DEFAULT NULL COMMENT '性别(1男 2女)',
  `password` varchar(255)  DEFAULT NULL COMMENT '密码',
  `email` varchar(100)  DEFAULT NULL COMMENT '邮箱',
  `phoneNumber` varchar(100) DEFAULT NULL COMMENT '电话号码',
  `is_enable` varchar(10)  DEFAULT NULL COMMENT '是否启用 0禁用1启用',
  `headPortrait` varchar(50)  DEFAULT NULL COMMENT '头像',
  `duty` varchar(50)  DEFAULT NULL COMMENT '岗位',
  `post` varchar(50)  DEFAULT NULL COMMENT '职位',
  `dept` varchar(50)  DEFAULT NULL COMMENT '部门',
  `adcd` varchar(50)  DEFAULT NULL COMMENT '行政区划',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `sys_dept_record` (
  `id` varchar(40) NOT NULL COMMENT '主键id',
  `pId` varchar(40) DEFAULT NULL COMMENT '上级部门id',
  `deptcode` varchar(20) DEFAULT NULL COMMENT '部门编号',
  `deptName` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `adcd` varchar(20) DEFAULT NULL COMMENT '行政区划',
  `deptHead` varchar(50)   NULL COMMENT '部门联系人',
  `deptType` varchar(10)  DEFDEFAULTAULT NULL COMMENT '机构类型(0:内部机构;1:外部机构)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
