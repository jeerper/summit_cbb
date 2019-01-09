# 山脉科技共享组件平台

## 组件基本信息

|组件名称|组件工程名|生产服务器组件访问地址|本地运行默认端口|负责人|
|--|--|--|--|--|
|注册中心|cbb_register_center|192.168.140.155:45000|8761|刘源|
|API网关|cbb_api_gateway_db|192.168.140.155:45001|22222|刘源|
|用户,角色,菜单管理组件|cbb_userauth|192.168.140.155:45002|22224|成虎|
|开发新组件的模板|cbb_demo|192.168.140.155:45003|22223|刘源|
|雨情组件|cbb_pptn|❓|❓|❓|
|天气组件|cbb_weather|❓|❓|何亚楠|
|测站管理组件|cbb_station|❓|❓|成虎|
|消息通知组件|cbb_notify|❓|❓|寇振华|
|组态通信组件|cbb_scada|❓|❓|刘源|
|数据转移组件|cbb_etl|❓|❓|❓|
|视频组件|cbb_video|❓|❓|❓|

## 开发环境

- git
- jdk 1.8+
- maven 3.2+
- redis 3.2+
- mysql 5.7+
- IDE Idea&Eclipse
- lombok

## 父工程结构

```
cbb_parent
├── cbb_api_gateway_db  --API网关组件(数据源基于Mysql-->目前采用的方案)
├── cbb_api_gateway_es  --API网关组件(数据源基于Elasticsearch)
├── cbb_common          --公共依赖库
├── cbb_demo            --开发新组件的模板
├── cbb_pptn            --水雨情组件
├── cbb_register_center --注册中心组件
├── cbb_userauth        --用户,角色,菜单管理组件
├── cbb_weather         --天气组件
├── env                 --生产环境的环境变量存储文件夹
└── pom.xml             --主工程的maven配置文件
```

## 子工程 module目录结构

```
├── backend  --组件源代码工程
│   ├── src  --源码目录
│   │   └── main
│   │       ├── java
│   │       │    └── com.summit
│   │       │         └── MainAction.java --组件的启动类(main函数入口)
│   │       └── resources
│   │            ├── application.yml       --公共变量配置文件
│   │            ├── application-dev.yml   --开发环境配置文件
│   │            ├── application-pro.yml   --生产环境配置文件
│   │            └── logback.xml           --日志配置文件
│   ├── .dockerignore       --构建Docker镜像时复制文件的忽略文件清单
│   ├── Dockerfile-backend  --构建Docker镜像脚本
│   └── pom.xml             --组件的maven配置文件
├── deploy                  --部署生产环境相关脚本
└── pom.xml                 --module的maven配置文件
```

## 启动

### 所有组件本地启动

#### 必须启动的组件清单

|组件名称|组件工程名|端口|备注|
|--|--|--|--|
|注册中心|cbb_register_center|8761|这是在本地搭建共享组件平台的首要启动组件,用于发现各个启动的组件|
|API网关|cbb_api_gateway_db|22222|网关是外部系统访问各个共享组件的唯一入口,包括外部系统API调试时的Swagger的入口,路由到各个服务,给API调用方分发token,访问鉴权|
|用户,角色,菜单管理组件|cbb_userauth|22224|用于提供用户，角色，可访问功能等信息，协助网关进行共享组件访问鉴权|

#### 修改所有要启动的组件的配置文件

- 切换到开发环境模式配置文件,需要配置组件中的`application.yml`配置文件:
    ``` yml
    spring:
      profiles:
        active: dev --改为dev
    ```

- 并修改`application-dev.yml`配置文件中的:
    ``` yml
    spring:
      redis: localhost --改为lcoalhost地址
    eureka:
      client:
        serviceUrl:
          defaultZone: "http://ucp:Summit2018@localhost:8761/eureka/" --注册中心组件默认端口是8761，所以注册中心组件无需配置，所以直接启动即可
    ```
- 根据上述组件启动清单，依次启动组件，运行`MainAction.java`
- 各个组件启动完成后访问`http://localhost:8761/admin/login`,查看各个组件运行状态，访问SpringBootAdmin的用户名和密码分别是：
    ```
      用户名:ucp
      密码:Summit2018
     ```
- 当出现如下状态，<font color=green size=5px>all up</font> 则表示组件运行正常:

![SpringBootAdmin监控](doc/SpringBootAdmin.png)

- 各个组件启动完成后访问`http://localhost:22222/swagger-ui.html`,这个界面来进行接口调试，入下图所示：
 
  ![Swagger接口调试界面](doc/Swagger-test-ui.png)