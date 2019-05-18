CREATE TABLE `sys_ad_cd` (
  `ADCD` varchar(15) NOT NULL COMMENT '行政区划编码',
  `ADNM` varchar(100) DEFAULT NULL COMMENT '行政区划名称',
  `PADCD` varchar(15) DEFAULT NULL COMMENT '父节点',
  `ADLEVEL` varchar(1) DEFAULT NULL COMMENT '级别（省1，市2，县3，乡4，村5）',
  PRIMARY KEY (`ADCD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_dept` (
  `ID` varchar(40) NOT NULL COMMENT '主键id',
  `PID` varchar(40) DEFAULT NULL COMMENT '上级部门id',
  `DEPTCODE` varchar(20) DEFAULT NULL COMMENT '部门编号',
  `DEPTNAME` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `ADCD` varchar(20) DEFAULT NULL COMMENT '行政区划',
  `REMARK` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_dictionary` (
  `CODE` varchar(50) NOT NULL COMMENT '编码',
  `PCODE` varchar(50) DEFAULT NULL COMMENT '上级编号',
  `NAME` varchar(50) NOT NULL COMMENT '名称',
  `CKEY` varchar(50) DEFAULT NULL COMMENT '每个code唯一key',
  `NOTE` text COMMENT '备注',
  PRIMARY KEY (`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_function` (
  `ID` varchar(40) NOT NULL COMMENT '主键',
  `PID` varchar(40) DEFAULT NULL COMMENT '上级菜单id',
  `NAME` varchar(40) NOT NULL COMMENT '菜单名称',
  `FDESC` int(11) DEFAULT NULL COMMENT '序号',
  `IS_ENABLED` int(11) NOT NULL COMMENT '是否启用 0禁用1启用',
  `FURL` text COMMENT '菜单路径',
  `IMGULR` text COMMENT '图片路径',
  `NOTE` text COMMENT '备注',
  `SUPER_FUN` int(11) DEFAULT NULL COMMENT '超级功能 1是 0否',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_role` (
  `CODE` varchar(40) NOT NULL COMMENT '角色编号，自动生成ROLE_XXX',
  `NAME` varchar(40) NOT NULL COMMENT '角色名称',
  `NOTE` text COMMENT '备注',
  PRIMARY KEY (`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_role_function` (
  `ID` varchar(40) NOT NULL COMMENT '主键',
  `ROLE_CODE` varchar(40) DEFAULT NULL COMMENT '角色code',
  `FUNCTION_ID` varchar(40) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_user` (
  `USERNAME` varchar(50) NOT NULL COMMENT '用户登录名',
  `NAME` varchar(50) NOT NULL COMMENT '用户姓名',
  `SEX` varchar(2) DEFAULT NULL COMMENT '性别 1男 2女',
  `PASSWORD` varchar(255) NOT NULL COMMENT '密码',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `PHONE_NUMBER` varchar(255) DEFAULT NULL COMMENT '电话号码',
  `IS_ENABLED` int(11) NOT NULL COMMENT '是否启用 0禁用1启用',
  `LAST_UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后更新时间',
  `STATE` int(11) NOT NULL COMMENT '是否删除  0已删除1正常',
  `NOTE` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`USERNAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_user_adcd` (
  `ID` varchar(42) NOT NULL COMMENT '主键',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `ADCD` varchar(30) DEFAULT NULL COMMENT '行政区划编码',
  `CREATETIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_user_dept` (
  `ID` varchar(40) NOT NULL COMMENT '主键',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `DEPTID` varchar(40) DEFAULT NULL COMMENT '部门id',
  `CREATETIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_user_role` (
  `ID` varchar(40) NOT NULL COMMENT '主键',
  `USERNAME` varchar(40) DEFAULT NULL COMMENT '用户名',
  `ROLE_CODE` varchar(40) DEFAULT NULL COMMENT '角色code',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_log` (
  `id` varchar(40) NOT NULL COMMENT '主键',
  `userName` varchar(100) DEFAULT NULL COMMENT '登录用户名',
  `callerIP` varchar(100) DEFAULT NULL COMMENT '访问机器IP',
  `funName` varchar(255) DEFAULT NULL COMMENT '访问模块名称',
  `sTime` datetime DEFAULT NULL COMMENT '访问开始时间',
  `erroInfo` longtext COMMENT '错误信息',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `actionFlag` varchar(1) DEFAULT NULL COMMENT '调用是否成功标志  1：成功  0：失败',
  `operType` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_sTimeasc` (`sTime`) USING BTREE,
  KEY `index_sTimedesc` (`sTime`) USING BTREE,
  KEY `index_eTimeasc` (`updateTime`) USING BTREE,
  KEY `index_eTimedesc` (`updateTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
