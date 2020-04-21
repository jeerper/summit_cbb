alter table sys_user_record add column state int(11) NULL DEFAULT NULL  COMMENT '是否删除  0已删除1正常';
alter table sys_user_auth add column state_auth int(11) NULL DEFAULT NULL  COMMENT '是否删除  0已删除1正常';