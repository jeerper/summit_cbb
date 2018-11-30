#!/bin/sh -l

#此处用于存放公共变量

#注册中心端口
Registry_Center_Port=30008






#性能监控控制台账号
spring_boot_admin_username=ucp
#性能监控控制台密码
spring_boot_admin_password=Summit2018
#注册中心IP
Registry_Center_IP=172.17.2.226
#注册中心URL
Registry_Center_URL="http://${spring_boot_admin_username}:${spring_boot_admin_password}@${Registry_Center_IP}:${Registry_Center_Port}/eureka/"
