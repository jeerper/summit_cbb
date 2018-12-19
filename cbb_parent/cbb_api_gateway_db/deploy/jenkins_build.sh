#!/bin/sh -l
#项目名称
projectName="cbb_api_gateway_db"

source ${WORKSPACE}/cbb_parent/env/env.sh

sed -i 's,${projectName},'${projectName}',g' ./.env

sed -i 's,${WebServer_Port},'${ApiGateway_Port}',g' ./.env

sed -i 's,${Registry_Center_URL},'${Registry_Center_URL}',g' ./.env



echo "编译cbb_register_center"

cd ${WORKSPACE}/cbb_parent

mvn clean

mvn install


cd ${WORKSPACE}/cbb_parent/${projectName}/deploy

echo "部署cbb_register_center(Docker版本)"


docker-compose -p ${projectName} down  --rmi all

docker-compose -p ${projectName} up  -d


echo "打包cbb_register_center(Windows版本)"

if [ ! -d ${fileStoragePath}/${projectName} ]
then
  echo "装载java后台启动程序包装模板"
  cp -rf ${java_service_wrapper_template_path} ${fileStoragePath}/${projectName}
  sed -i 's,${AppName},'${projectName}',g' ${fileStoragePath}/${projectName}/conf/wrapper.conf
fi

cd ${WORKSPACE}/cbb_parent/${projectName}/backend/target

cp -rf backend.jar ${fileStoragePath}/${projectName}/bin
cp -rf lib ${fileStoragePath}/${projectName}/bin/lib
cp -rf config ${fileStoragePath}/${projectName}/bin/config