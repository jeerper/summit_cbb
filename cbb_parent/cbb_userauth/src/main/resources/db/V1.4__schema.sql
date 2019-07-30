alter table sys_user add column DUTY varchar(40) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '岗位';
alter table sys_user add column COMPANY varchar(200) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '工作单位';
alter table sys_user add column POST varchar(50) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '职务';
alter table sys_user add column SN varchar(40) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '序号';