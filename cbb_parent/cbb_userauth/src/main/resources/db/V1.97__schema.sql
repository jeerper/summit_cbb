﻿alter table sys_user add column isAudited varchar(10) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '审核是否通过(0：发起审核，1：通过，2：不通过)';
alter table sys_dept add column isAudited varchar(10) CHARACTER SET utf8 COLLATE utf8_bin COMMENT '审核是否通过(0：发起审核，1：通过，2：不通过)';