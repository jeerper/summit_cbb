#!/bin/sh -l
set -e
#项目名称
projectName="cbb_register_center"
#组件名称
component_name="注册中心"
#组件描述
component_description="注册中心"
#组件标签
component_tag=${projectName}

source ${WORKSPACE}/cbb_parent/env/env.sh

sed -i 's,${projectName},'${projectName}',g' ./.env

sed -i 's,${WebServer_Port},'${Registry_Center_Port}',g' ./.env

sed -i 's,${WebServer_Port},'${Registry_Center_Port}',g' ${WORKSPACE}/cbb_parent/${projectName}/src/main/resources/application-pro.yml


sed -i 's,${spring_boot_admin_username},'${spring_boot_admin_username}',g' ./.env
sed -i 's,${spring_boot_admin_password},'${spring_boot_admin_password}',g' ./.env


echo "编译"${projectName}

cd ${WORKSPACE}/cbb_parent

mvn clean

mvn deploy


if [ $? = 0 ]
then
 echo "maven构建成功"
else
 exit 1
fi


cd ${WORKSPACE}/cbb_parent/${projectName}/deploy

echo "部署"${projectName}"(Docker版本)"


docker-compose -p ${projectName} down  --rmi all

docker-compose -p ${projectName} up  -d

if [ ! -d ${java_service_wrapper_template_path} ]
then
    exit 0
fi


echo "打包"${projectName}"(Windows版本)"

if [ ! -d ${fileStoragePath}/${projectName} ]
then
  echo "装载java后台启动程序包装模板"
  cp -rf ${java_service_wrapper_template_path} ${fileStoragePath}/${projectName}
  sed -i 's,${AppName},'${projectName}',g' ${fileStoragePath}/${projectName}/conf/wrapper.conf
fi

cd ${WORKSPACE}/cbb_parent/${projectName}/target

rm -rf ${fileStoragePath}/${projectName}/bin/backend.jar
rm -rf ${fileStoragePath}/${projectName}/bin/lib
rm -rf ${fileStoragePath}/${projectName}/bin/config

cp -rf backend.jar ${fileStoragePath}/${projectName}/bin
cp -rf lib ${fileStoragePath}/${projectName}/bin/
cp -rf config ${fileStoragePath}/${projectName}/bin/

echo "推送组件信息到组件货架上"
curl -X POST "http://${Cbb_Store_Server_Url}/api/component/pushComponentInfo" -H "accept: */*" -H "Content-Type: application/json;charset=UTF-8" -d "{ \"description\": \"${component_description}\", \"name\": \"${component_name}\", \"tag\": \"${component_tag}\", \"type\": \"common\"}"