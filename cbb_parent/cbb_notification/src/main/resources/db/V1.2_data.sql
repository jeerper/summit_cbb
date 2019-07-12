

insert into sms_template (template_id,template_content,template_code,template_type,create_time,update_time)
	VALUES('template001','验证码${code}，您正在登录，若非本人操作，请勿泄露。','SMS_168096422','验证码',now(),now());
	
insert into sms_template (template_id,template_content,template_code,template_type,create_time,update_time)
	VALUES('template002','验证码${code}，您正在进行身份验证，打死不要告诉别人哦！','SMS_168096423','验证码',now(),now());