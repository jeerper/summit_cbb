INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('sex', 'root', '性别', 'sex', '性别')
ON DUPLICATE KEY UPDATE CODE='sex';
INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('sex_1', 'sex', '女', '1', NULL)
ON DUPLICATE KEY UPDATE CODE='sex_1';
INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('sex_2', 'sex', '男', '2', NULL)
ON DUPLICATE KEY UPDATE CODE='sex_2';
INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('isEnabled', 'root', '启用状态', NULL, NULL)
ON DUPLICATE KEY UPDATE CODE='isEnabled';
INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('isEnabled_1', 'isEnabled', '启用', '1', NULL)
ON DUPLICATE KEY UPDATE CODE='isEnabled_1';
INSERT INTO `sys_dictionary` (`CODE`, `PCODE`, `NAME`, `CKEY`, `NOTE`) VALUES ('isEnabled_0', 'isEnabled', '禁用', '0', NULL)
ON DUPLICATE KEY UPDATE CODE='isEnabled_0';
