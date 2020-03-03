alter table sys_user_auth modify column dept_auth varchar(500);
alter table sys_user_auth modify column adcd_auth varchar(500);
alter table sys_user_auth add column submitted_to  varchar(30) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '提交至哪个部门';
alter table sys_dept_auth add column submitted_to  varchar(30) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '提交至哪个部门';