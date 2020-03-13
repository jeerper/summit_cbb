CREATE TABLE IF NOT EXISTS `sys_auth`(
  `id` varchar(50) NOT NULL COMMENT '主键',
  `apply_name` varchar(50) NOT NULL COMMENT '申请人姓名',
  `apply_type` varchar(50)  DEFAULT NULL COMMENT '申请类型：(0:机构申请，1：用户申请)',
  `submitted_to` varchar(50)  DEFAULT NULL COMMENT '提交至哪个部门',
  `isAudited` varchar(10)  DEFAULT NULL COMMENT '审核是否通过(0：待处理，1：通过，2：不通过)',
  `apply_time` datetime  DEFAULT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;