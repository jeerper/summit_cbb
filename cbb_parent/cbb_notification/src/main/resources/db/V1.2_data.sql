
INSERT INTO `sms_template` VALUES ('template001', '验证码${code}，您正在登录，若非本人操作，请勿泄露。', 'SMS_168096422', '验证码', now(),now());
INSERT INTO `sms_template` VALUES ('template002', '验证码${code}，您正在进行身份验证，打死不要告诉别人哦！', 'SMS_168096423', '验证码', now(),now());
INSERT INTO `sms_template` VALUES ('template003', '${name}您好！${reachname}您有新的巡河任务(${start}至${end}，${rate}次/${period})，请根据任务要求合理安排巡河。', 'SMS_170351846', '通知类', now(),now());
INSERT INTO `sms_template` VALUES ('template004', '${name}您好！${reachname}任务(${start}至${end})您应巡河${num1}次,本${datetype}您已巡河${num2}次,请注意及时巡河。', 'SMS_170351865', '通知类', now(),now());