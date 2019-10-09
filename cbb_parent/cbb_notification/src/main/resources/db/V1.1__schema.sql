use `cbb_notification`;

drop table if exists msg_email;
/*==============================================================*/
/* Table: msg_email                                             */
/*==============================================================*/
create table msg_email 
(
   email_id             varchar(32)      not null     comment '�ʼ����',
   email_name           varchar(64)      null         comment '�ʼ�����',
   send_to              varchar(64)      not null     comment '�����ߵ�ַ',
   email_title          varchar(256)     not null     comment '�ʼ�����',
   email_content        text             not null     comment '�ʼ�����',
   send_state           integer          null         comment '����״̬',
   create_time          datetime         null         comment '����ʱ��',
   update_time          datetime         null         comment '����ʱ��',
   primary key (email_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '�ʼ���¼��';



drop table if exists msg_sms;
/*==============================================================*/
/* Table: msg_sms                                               */
/*==============================================================*/
create table msg_sms 
(
   sms_id            varchar(32)     not null   comment '���ű��',
   template_id       varchar(32)     not null   comment 'ģ��id',
   res_phone         varchar(20)     not null   comment '�������ֻ���',
   biz_id            varchar(64)     null       comment '���ͻ�ִID',
   sms_signname      varchar(32)     not null   comment '����ǩ��',
   sms_content       text            not null   comment '��������',
   send_state        integer         null       comment '����״̬',
   create_time       datetime        null       comment '����ʱ��',
   update_time       datetime        null       comment '����ʱ��',
   primary key (sms_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '���ż�¼��';




drop table if exists sms_template;
/*==============================================================*/
/* Table: sms_template                                          */
/*==============================================================*/
create table sms_template 
(
   template_id          varchar(32)   not null   comment 'ģ����',
   template_content     varchar(255)   null       comment 'ģ������',
   template_code        varchar(32)   not null   comment 'ģ���',
   template_type        varchar(16)   null       comment 'ģ������',
   create_time          datetime      null       comment '����ʱ��',
   update_time          datetime      null       comment '����ʱ��',
   primary key (template_id)
) ENGINE=InnoDB default CHARSET=utf8 comment '����ģ���';



