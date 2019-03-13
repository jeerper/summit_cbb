CREATE TABLE `ad_cd_b` (
  `ADCD` varchar(15) NOT NULL COMMENT '行政区划编码',
  `ADNM` varchar(100) DEFAULT NULL COMMENT '行政区划名称',
  `PADCD` varchar(15) DEFAULT NULL COMMENT '父节点',
  `ADLEVEL` varchar(1) DEFAULT NULL COMMENT '级别（省1，市2，县3，乡4，村5）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(48) NOT NULL,
  `resource_ids` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` text,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_dept` (
  `ID` varchar(40) NOT NULL,
  `PID` varchar(40) DEFAULT NULL,
  `DEPTCODE` varchar(20) DEFAULT NULL,
  `DEPTNAME` varchar(200) DEFAULT NULL,
  `REMARK` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_dictionary` (
  `CODE` varchar(50) NOT NULL,
  `PCODE` varchar(50) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `CKEY` varchar(50) DEFAULT NULL,
  `NOTE` text,
  PRIMARY KEY (`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_function` (
  `ID` varchar(40) NOT NULL,
  `PID` varchar(40) DEFAULT NULL,
  `NAME` varchar(40) NOT NULL,
  `FDESC` int(11) DEFAULT NULL,
  `IS_ENABLED` int(11) NOT NULL COMMENT '0禁用1启用',
  `FURL` text,
  `IMGULR` text,
  `NOTE` text,
  `SUPER_FUN` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_role` (
  `CODE` varchar(40) NOT NULL,
  `NAME` varchar(40) NOT NULL,
  `NOTE` text,
  PRIMARY KEY (`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_role_function` (
  `ID` varchar(40) NOT NULL,
  `ROLE_CODE` varchar(40) DEFAULT NULL,
  `FUNCTION_ID` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_user` (
  `NAME` varchar(50) NOT NULL COMMENT '用户姓名',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户登录名',
  `PASSWORD` varchar(255) NOT NULL COMMENT '密码',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `PHONE_NUMBER` varchar(255) DEFAULT NULL COMMENT '电话号码',
  `IS_ENABLED` int(11) NOT NULL COMMENT '0禁用1启用',
  `LAST_UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后更新时间',
  `STATE` int(11) NOT NULL COMMENT '0已删除1正常',
  `NOTE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_user_role` (
  `ID` varchar(40) NOT NULL,
  `USERNAME` varchar(40) DEFAULT NULL,
  `ROLE_CODE` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `wf_sys_log`;
CREATE TABLE `wf_sys_log` (
  `id` varchar(40) NOT NULL COMMENT '主键',
  `userName` varchar(100) DEFAULT NULL COMMENT '登录用户名',
  `callerIP` varchar(100) DEFAULT NULL COMMENT '访问机器IP',
  `funName` varchar(255) DEFAULT NULL COMMENT '访问模块名称',
  `sTime` datetime(3) DEFAULT NULL COMMENT '访问开始时间',
  `actionTime` int(11) DEFAULT NULL COMMENT '执行时间',
  `erroInfo` longtext COMMENT '错误信息',
  `eTime` datetime(3) DEFAULT NULL,
  `actionFlag` varchar(1) DEFAULT NULL COMMENT '调用是否成功标志  1：成功  0：失败',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_sTimeasc` (`sTime`) USING BTREE,
  KEY `index_sTimedesc` (`sTime`) USING BTREE,
  KEY `index_eTimeasc` (`eTime`) USING BTREE,
  KEY `index_eTimedesc` (`eTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;