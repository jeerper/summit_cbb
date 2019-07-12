
drop DATABASE if exists `ccb_notification`;

CREATE DATABASE `ccb_notification` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

use `ccb_notification`;

drop table if exists msg_email;
/*==============================================================*/
/* Table: msg_email                                             */
/*==============================================================*/
create table msg_email 
(
   email_id             varchar(32)      not null     comment '邮件编号',
   email_name           varchar(64)      null         comment '邮件名称',
   send_to              varchar(64)      not null     comment '接收者地址',
   email_title          varchar(256)     not null     comment '邮件标题',
   email_content        text             not null     comment '邮件内容',
   send_state           integer          null         comment '发送状态',
   create_time          datetime         null         comment '创建时间',
   update_time          datetime         null         comment '更新时间',
   primary key (email_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '邮件记录表';



drop table if exists msg_sms;
/*==============================================================*/
/* Table: msg_sms                                               */
/*==============================================================*/
create table msg_sms 
(
   sms_id            varchar(32)     not null   comment '短信编号',
   template_id       varchar(32)     not null   comment '模板id',
   res_phone         varchar(20)     not null   comment '接收者手机号',
   biz_id            varchar(64)     null       comment '发送回执ID',
   sms_signname      varchar(32)     not null   comment '短信签名',
   sms_content       text            not null   comment '短信内容',
   send_state        integer         null       comment '发送状态',
   create_time       datetime        null       comment '创建时间',
   update_time       datetime        null       comment '更新时间',
   primary key (sms_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '短信记录表';




drop table if exists sms_template;
/*==============================================================*/
/* Table: sms_template                                          */
/*==============================================================*/
create table sms_template 
(
   template_id          varchar(32)   not null   comment '模板编号',
   template_content     varchar(32)   null       comment '模板内容',
   template_code        varchar(32)   not null   comment '模板号',
   template_type        varchar(16)   null       comment '模板类型',
   create_time          datetime      null       comment '创建时间',
   update_time          datetime      null       comment '更新时间',
   primary key (template_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '短信模板表';



