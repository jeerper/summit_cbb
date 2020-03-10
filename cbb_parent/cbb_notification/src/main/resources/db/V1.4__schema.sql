DROP TABLE IF EXISTS `msg_email_result`;
DROP TABLE IF EXISTS `msg_email`;

CREATE TABLE `msg_email`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件发送记录ID',
  `email_title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件标题',
  `email_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件内容',
  `file_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件保存地址',
  `send_state` int(1) NOT NULL COMMENT '发送状态 1发送中 2发送完成 3发送异常',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邮件发送表' ROW_FORMAT = Compact;

CREATE TABLE `msg_email_result`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件结果ID',
  `record_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件发送记录ID',
  `send_to` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收人邮件地址',
  `send_result` int(1) NULL DEFAULT NULL COMMENT '发送结果 1发送成功 2发送失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邮件发送结果表' ROW_FORMAT = Compact;


alter table sms_template modify column template_id varchar(32) comment '模板ID';
alter table sms_template modify column template_content varchar(255) comment '短信模板内容';
alter table sms_template modify column template_code varchar(32) comment '模板编码';
alter table sms_template modify column template_type varchar(16) comment '模板类型';
alter table sms_template modify column create_time datetime comment '创建时间';
alter table sms_template modify column update_time datetime comment '更新时间';

alter table msg_sms modify column sms_id varchar(32) comment '短信ID';
alter table msg_sms modify column template_id varchar(32) comment '短信模板ID';
alter table msg_sms modify column res_phone varchar(20) comment '接收短信的手机号码';
alter table msg_sms modify column biz_id varchar(64) comment '发送回执ID';
alter table msg_sms modify column sms_signname varchar(32) comment '短信签名';
alter table msg_sms modify column sms_content text comment '短信内容';
alter table msg_sms modify column send_state int(11) comment '发送状态';
alter table msg_sms modify column create_time datetime comment '创建时间';
alter table msg_sms modify column update_time datetime comment '更新时间';

