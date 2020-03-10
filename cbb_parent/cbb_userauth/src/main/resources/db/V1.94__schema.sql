CREATE TABLE IF NOT EXISTS `sys_login_log`
(
    `id`         varchar(40)  NOT NULL COMMENT '主键',
    `userName`   varchar(100) NOT NULL COMMENT '登录用户名',
    `callerIP`   varchar(100) NOT NULL COMMENT '登陆机器IP',
    `loginTime`  datetime     NOT NULL COMMENT '登陆时间',
    `onlineTime` int(11)      NOT NULL COMMENT '在线时长',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
