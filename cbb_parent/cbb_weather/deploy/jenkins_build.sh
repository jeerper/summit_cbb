#!/bin/sh -l
#项目名称
projectName="cbb_weather"
#组件名称
component_name="天气组件"
#组件描述
component_description="天气组件"
#组件标签
component_tag=${projectName}

source ${WORKSPACE}/cbb_parent/env/env.sh

sed -i 's,${projectName},'${projectName}',g' ./.env

sed -i 's,${WebServer_Port},'${Weather_Port}',g' ./.env

sed -i 's,${WebServer_Port},'${Weather_Port}',g' ${WORKSPACE}/cbb_parent/${projectName}/src/main/resources/application-pro.yml

sed -i 's,${Registry_Center_URL},'${Registry_Center_URL}',g' ${WORKSPACE}/cbb_parent/${projectName}/src/main/resources/application-pro.yml


sed -i 's,${Registry_Center_URL},'${Registry_Center_URL}',g' ./.env

sed -i 's,${Registry_Center_IP},'${Registry_Center_IP}',g' ./.env



echo "编译"${projectName}

cd ${WORKSPACE}/cbb_parent

mvn clean

mvn deploy


cd ${WORKSPACE}/cbb_parent/${projectName}/deploy

echo "部署"${projectName}"(Docker版本)"


docker-compose -p ${projectName} down  --rmi all

docker-compose -p ${projectName} up  -d


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
curl -X POST "http://${Registry_Center_IP}:43000/component/pushComponentInfo" -H "accept: */*" -H "Content-Type: application/json;charset=UTF-8" -d "{ \"description\": \"${component_description}\", \"name\": \"${component_name}\", \"tag\": \"${component_tag}\"}"