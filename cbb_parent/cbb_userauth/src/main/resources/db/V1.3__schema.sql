ALTER TABLE `sys_log` ADD  updateTime  datetime(3) DEFAULT NULL COMMENT '结束日期';
ALTER TABLE `sys_log` ADD  operType  varchar(1) DEFAULT NULL COMMENT '操作类型(1：新增,  2：修改,   3：删除,  4：授权 ,  5：查询  )';
ALTER TABLE `sys_log` ADD  `systemName`  varchar(100) DEFAULT NULL COMMENT '系统名称';
ALTER TABLE `sys_log` ADD  `describe1`  varchar(500) DEFAULT NULL COMMENT '操作描述';

