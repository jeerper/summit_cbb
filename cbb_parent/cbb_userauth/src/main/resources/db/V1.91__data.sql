DELETE FROM sys_role_function WHERE ROLE_CODE = 'ROLE_SUPERUSER';
DELETE FROM sys_function WHERE ID = '71a3f970d03f429889f3a9ff2793419c';
DELETE FROM sys_function WHERE ID = '3e0183a912f949779d4157b3241455ff';
DELETE FROM sys_function WHERE ID = '45a2d1f0dac741739884e4f0b60d747b';
DELETE FROM sys_function WHERE ID = 'd276316d34794ac4beb8afd11b9adcec';
INSERT IGNORE INTO `sys_function` VALUES ('e5aa55ee586546b7ac5a707d5b82a244', 'root', '系统管理', 20, 1, '', 'team', '系统设置', 0);
INSERT IGNORE INTO `sys_function` VALUES ('eff36a373e84463397186861633b3d4b', 'e5aa55ee586546b7ac5a707d5b82a244', '用户管理', 1, 1, 'System/user', NULL, '用户设置', 0);
INSERT IGNORE INTO `sys_function` VALUES ('9ee3cdb8c944477d8b2b5cbb6eb6793b', 'e5aa55ee586546b7ac5a707d5b82a244', '角色管理', 2, 1, 'System/role', NULL, NULL, 0);
INSERT IGNORE INTO `sys_function` VALUES ('f1070bcdd9d84c43945c0a304700a24f', 'e5aa55ee586546b7ac5a707d5b82a244', '功能管理', 3, 1, 'System/function', NULL, NULL, 0);
update sys_function set PID='e5aa55ee586546b7ac5a707d5b82a244' where PID='3e0183a912f949779d4157b3241455ff';

