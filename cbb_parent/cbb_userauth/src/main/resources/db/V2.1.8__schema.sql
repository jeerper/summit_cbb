alter table sys_dept_record add column deptId varchar(50) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '部门id';
alter table sys_dept_auth add column deptRecord_id varchar(50) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '部门记录表id';
alter table sys_user_record drop primary key;
alter table sys_user_record add column id varchar(50) primary key;
alter table sys_user_auth add column userRecord_id varchar(50) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '用户记录表id';
