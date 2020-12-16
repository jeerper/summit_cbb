use `cbb_notification`;

drop table if exists msg_email;
/*==============================================================*/
/* Table: msg_email                                             */
/*==============================================================*/
create table msg_email 
(
   email_id             varchar(32)      not null     comment  '',
   email_name           varchar(64)      null         comment  '',
   send_to              varchar(64)      not null     comment  '',
   email_title          varchar(256)     not null     comment  '',
   email_content        text             not null     comment  '',
   send_state           integer          null         comment  '' ,
   create_time          datetime         null         comment  '',
   update_time          datetime         null         comment  '',
   primary key (email_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '';



drop table if exists msg_sms;
/*==============================================================*/
/* Table: msg_sms                                               */
/*==============================================================*/
create table msg_sms 
(
   sms_id            varchar(32)     not null   comment  '',
   template_id       varchar(32)     not null   comment  '',
   res_phone         varchar(20)     not null   comment  '',
   biz_id            varchar(64)     null       comment  '',
   sms_signname      varchar(32)     not null   comment  '',
   sms_content       text            not null   comment  '',
   send_state        integer         null       comment  '',
   create_time       datetime        null       comment  '',
   update_time       datetime        null       comment  '',
   primary key (sms_id)
) ENGINE=InnoDB default CHARSET=utf8 comment  '';




drop table if exists sms_template;
/*==============================================================*/
/* Table: sms_template                                          */
/*==============================================================*/
create table sms_template 
(
   template_id          varchar(32)   not null   comment  '',
   template_content     varchar(255)   null       comment  '',
   template_code        varchar(32)   not null   comment  '',
   template_type        varchar(16)   null       comment  '',
   create_time          datetime      null       comment  '',
   update_time          datetime      null       comment  '',
   primary key (template_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '';



