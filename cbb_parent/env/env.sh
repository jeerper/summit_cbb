#!/bin/sh -l

#此处用于存放公共变量

#注册中心端口
Registry_Center_Port=45000
#API网关端口
ApiGateway_Port=45001
#用户管理端口
UserAuth_Port=45002
#Demo端口
Demo_Port=45003
#天气组件端口
Weather_Port=45004


#文件仓库地址
fileStoragePath="/home/cbb_store_server_file_storage"
#java后台启动程序包装模板
java_service_wrapper_template_path="${fileStoragePath}/java_service_wrapper_template"


#性能监控控制台账号
spring_boot_admin_username="ucp"
#性能监控控制台密码
spring_boot_admin_password="Summit2018"
#注册中心IP
Registry_Center_IP="192.168.140.155"
#注册中心URL
Registry_Center_URL="http://${spring_boot_admin_username}:${spring_boot_admin_password}@${Registry_Center_IP}:${Registry_Center_Port}/eureka/"
