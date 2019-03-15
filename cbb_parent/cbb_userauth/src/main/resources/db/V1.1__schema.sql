CREATE TABLE `ad_cd_b` (
  `ADCD` varchar(15) NOT NULL COMMENT '������������',
  `ADNM` varchar(100) DEFAULT NULL COMMENT '������������',
  `PADCD` varchar(15) DEFAULT NULL COMMENT '���ڵ�',
  `ADLEVEL` varchar(1) DEFAULT NULL COMMENT '����ʡ1����2����3����4����5��'
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
  `IS_ENABLED` int(11) NOT NULL COMMENT '0����1����',
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
  `NAME` varchar(50) NOT NULL COMMENT '�û�����',
  `USERNAME` varchar(50) NOT NULL COMMENT '�û���¼��',
  `PASSWORD` varchar(255) NOT NULL COMMENT '����',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '����',
  `PHONE_NUMBER` varchar(255) DEFAULT NULL COMMENT '�绰����',
  `IS_ENABLED` int(11) NOT NULL COMMENT '0����1����',
  `LAST_UPDATE_TIME` datetime DEFAULT NULL COMMENT '������ʱ��',
  `STATE` int(11) NOT NULL COMMENT '0��ɾ��1����',
  `NOTE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sys_user_role` (
  `ID` varchar(40) NOT NULL,
  `USERNAME` varchar(40) DEFAULT NULL,
  `ROLE_CODE` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `wf_sys_log` (
  `id` varchar(40) NOT NULL COMMENT '����',
  `userName` varchar(100) DEFAULT NULL COMMENT '��¼�û���',
  `callerIP` varchar(100) DEFAULT NULL COMMENT '���ʻ���IP',
  `funName` varchar(255) DEFAULT NULL COMMENT '����ģ������',
  `sTime` datetime(3) DEFAULT NULL COMMENT '���ʿ�ʼʱ��',
  `actionTime` int(11) DEFAULT NULL COMMENT 'ִ��ʱ��',
  `erroInfo` longtext COMMENT '������Ϣ',
  `eTime` datetime(3) DEFAULT NULL,
  `actionFlag` varchar(1) DEFAULT NULL COMMENT '�����Ƿ�ɹ���־  1���ɹ�  0��ʧ��',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_sTimeasc` (`sTime`) USING BTREE,
  KEY `index_sTimedesc` (`sTime`) USING BTREE,
  KEY `index_eTimeasc` (`eTime`) USING BTREE,
  KEY `index_eTimedesc` (`eTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
