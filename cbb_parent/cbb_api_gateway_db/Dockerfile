###############################################################################
#                             第一阶段构建:Docker容器基础操作系统镜像
###############################################################################
FROM anapsix/alpine-java:8_server-jre_unlimited as operating_system

RUN echo "https://repo.huaweicloud.com/alpine/v3.4/main/" > /etc/apk/repositories \
 && echo "https://repo.huaweicloud.com/alpine/v3.4/community/" >> /etc/apk/repositories \
 && apk update \
 && apk add tzdata \
 && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
 && echo "Asia/Shanghai" >  /etc/timezone \
 && apk del tzdata \
 && rm -rf /var/cache/apk/*

###############################################################################
#                             第二阶段构建:将应用打包进容器
###############################################################################
FROM operating_system

MAINTAINER liuyuan <405653510@qq.com>

ADD ./target/  /opt/App/

WORKDIR /opt/App/

ENTRYPOINT ["java", "-jar"]

CMD ["backend.jar"]