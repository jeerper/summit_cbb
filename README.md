#  山脉科技共享组件平台
---

## 开发环境
- git
- jdk 1.8+
- maven 3.2+
- redis 3.2+
- mysql 5.7+
- IDE Idea&Eclipse
- lombok

## 工程结构

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

## module目录结构

```
├── backend 				--组件源代码工程
│   ├── src                 --源码目录
│   ├── .dockerignore       --构建Docker镜像时复制文件的忽略文件清单
│   ├── Dockerfile-backend  --构建Docker镜像脚本
│   └── pom.xml             --组件的maven配置文件
├── deploy                  --部署生产环境相关脚本
└── pom.xml                 --module的maven配置文件
```

## 启动

- 所有组件本地启动(至少需要启动以下组件)

|组件名称|组件工程名|端口|备注|
|--|--|--|--|
|注册中心|cbb_register_center|5432|  目前各应用共用1个实例，各应用可建不同的database|



2. ## 组件清单
* demo工程 `cbb_demo` 叶腾
* 基础包 `ccb_common` 叶腾
* API网关组件 `cbb_api_gateway` 叶腾
* 雨情组件 `cbb_pptn` 叶腾
* 天气组件 `cbb_weather` 何亚楠
* 测站管理组件 `cbb_station` 成虎
* 用户管理组件 `cbb_user` 成虎
* 消息通知组件 `cbb_notify` 寇振华
* 组态通信组件 `cbb_scada` 刘源
* 数据转移组件 `cbb_etl` ?
* 视频组件 `cbb_video` ?

