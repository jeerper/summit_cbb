#!/bin/sh -l
#项目名称
projectName="cbb_register_center"

source ${WORKSPACE}/cbb_parent/env/env.sh


sed -i 's,${WebServer_Port},'${Registry_Center_Port}',g' ./.env
sed -i 's,${spring_boot_admin_username},'${spring_boot_admin_username}',g' ./.env
sed -i 's,${spring_boot_admin_password},'${spring_boot_admin_password}',g' ./.env

echo "打包cbb_store_server"

cd ${WORKSPACE}/cbb_parent

#mvn clean
#
#mvn install

#echo "部署cbb_store_server"
#
#if [ ! -d ${fileStoragePath} ]
#then
#  echo "初始化基本文件夹"
#  mkdir  ${fileStoragePath}
#fi
#
#
#if [ ! -d ${java_service_wrapper_template_path} ]
#then
#  echo "初始化java后台启动程序包装模板文件夹"
#  cp -rf ${WORKSPACE}/deploy/java_service_wrapper_template ${java_service_wrapper_template_path}
#fi
#
#
#if [ ! -d ${java_jdk_path} ]
#then
#  echo "初始化jdk环境文件夹"
#  cp -rf ${WORKSPACE}/deploy/jdk ${java_jdk_path}
#fi
#
#
#
#
#cd ${WORKSPACE}/deploy
#
#docker-compose -p ${projectName} down  --rmi all
#
#docker-compose -p ${projectName} up  -d






