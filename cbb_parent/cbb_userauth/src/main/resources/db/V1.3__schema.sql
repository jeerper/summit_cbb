
DROP TABLE IF EXISTS 
CREATE TABLE `sys_log` (
  `id` varchar(40) NOT NULL COMMENT '主键',
  `userName` varchar(100) DEFAULT NULL COMMENT '登录用户名',
  `callerIP` varchar(100) DEFAULT NULL COMMENT '访问机器IP',
  `funName` varchar(255) DEFAULT NULL COMMENT '访问模块名称',
  `operType` varchar(1) DEFAULT NULL COMMENT '操作类型(1：新增,  2：修改,   3：删除,  4：授权 ,  5：查询  )',
  `stime` datetime(3) DEFAULT NULL COMMENT '访问开始时间',
  `erroInfo` longtext COMMENT '错误信息',
  `updateTime` datetime(3) DEFAULT NULL COMMENT '结束日期',
  `actionFlag` varchar(1) DEFAULT NULL COMMENT '调用是否成功标志  1：成功  0：失败',
  `systemName` varchar(100) DEFAULT NULL COMMENT '系统名称',
  `describe` varchar(500) DEFAULT NULL COMMENT '操作描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT character set utf8 collate utf8_bin;

