# 山脉科技共享组件平台

- [山脉科技共享组件平台](#山脉科技共享组件平台)
    - [共享组件货架Swagger访问地址](#共享组件货架swagger访问地址)
    - [共享组件平台Swagger访问地址](#共享组件平台swagger访问地址)
    - [jenkins访问地址](#jenkins访问地址)
    - [共享组件common包相关信息](#共享组件common包相关信息)
    - [组件基本信息](#组件基本信息)
    - [基础设施信息](#基础设施信息)
    - [开发环境](#开发环境)
    - [父工程结构](#父工程结构)
    - [子工程 module目录结构](#子工程-module目录结构)
    - [编译源代码](#编译源代码)
    - [组件名称命名规范](#组件名称命名规范)
    - [启动](#启动)
        - [方式一、所有组件本地启动](#方式一所有组件本地启动)
            - [必须启动的组件清单](#必须启动的组件清单)
            - [修改所有要启动的组件的配置文件](#修改所有要启动的组件的配置文件)
        - [方式二、只启动自己所要开发的组件](#方式二只启动自己所要开发的组件)
            - [修改自己所要开发的组件的配置文件](#修改自己所要开发的组件的配置文件)
    - [测试接口](#测试接口)
        - [登录](#登录)
    - [最后](#最后)
    - [关于共享组件发布到应用货架的注意事项:](#关于共享组件发布到应用货架的注意事项:)
    - [其他注意事项](#其他注意事项)
    - [后台接口状态码描述](#后台接口状态码描述)

## 共享组件货架Swagger访问地址

<http://192.168.140.155:43000/swagger-ui.html>

## 共享组件平台Swagger访问地址

<http://192.168.140.155:45001/swagger-ui.html>

## jenkins访问地址

<http://192.168.140.155:6333>

- 账号：root
- 密码：Summit2017

## 共享组件common包相关信息

- common包当前版本信息：`1.0.5.RELEASE`

- 更新内容：
  
  ```
  1.0.5.RELEASE: 把共享用户组件相关的实体添加到common中。
  ```

## 组件基本信息

|组件名称|组件工程名|生产服务器组件访问地址|本地运行默认端口|负责人|数据库名称|
|--|--|--|--|--|--|
|注册中心|cbb_register_center|192.168.140.155:45000|8761|刘源|无|
|API网关|cbb_api_gateway_db|192.168.140.155:45001|22222|刘源|cbb_api_gateway|
|用户,角色,菜单管理组件|cbb_userauth|192.168.140.155:45002|22224|刘永红|cbb_userauth|
|共享组件脚手架|cbb_demo|192.168.140.155:45003|22223|刘源|cbb_demo|
|雨情组件|cbb_pptn|❓|❓|❓|❓|
|天气组件|cbb_weather|192.168.140.155:45004|22225|何亚楠|cbb_weather|
|测站管理组件|cbb_station|❓|❓|成虎|❓|
|消息通知组件|cbb_notify|❓|❓|寇振华|❓|
|组态通信组件|cbb_scada|❓|❓|刘源|❓|
|数据转移组件|cbb_etl|❓|❓|❓|❓|
|视频组件|cbb_video|❓|❓|❓|❓|

## 基础设施信息

|名称|类型|访问地址|端口|用户名|密码|备注|
|--|--|--|--|--|--|--|
|Mysql|关系型数据库|192.168.140.155|49999|root|Summit2017|无|
|Redis|内存数据库|192.168.140.155|6379|无|无|无|

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
├── deploy  --部署生产环境相关脚本目录
├── src     --源码目录
│    └── main
│         ├── java
│         │    └── com.summit
│         │         └── MainAction.java --组件的启动类(main函数入口)
│         └── resources
│              ├── db                    --flyway管理的sql脚本目录
│              ├── application.yml       --公共变量配置文件
│              ├── application-dev.yml   --开发环境配置文件
│              ├── application-pro.yml   --生产环境配置文件
│              └── logback.xml           --日志配置文件
├── .dockerignore   --构建Docker镜像时复制文件的忽略文件清单
├── Dockerfile      --构建Docker镜像脚本
└── pom.xml         --module的maven配置文件
```

## 编译源代码

``` 
cd cbb_parent

mvn clean

mvn install
```

## 组件名称命名规范

- 配置组件中的`application.yml`配置文件:

  组件名称以`cbb-`开头，后缀以具体组件的用途的英文全拼或缩写命名，注意：用缩写的前提是必须是有明确意义的缩写

  ``` yml
    spring:
        application:
          name: "cbb-demo" -- 分隔符必须用'-'
  ```

- 配置组件中的`deploy/jenkins_build.sh`部署脚本:

  组件名称以`cbb_`开头,后缀以具体组件的用途的英文全拼或缩写命名，注意：用缩写的前提是必须是有明确意义的缩写

  ``` sh
    #项目名称
    projectName="cbb_api_gateway_db" -- 分隔符必须用'_'(下划线)，并且要保证该组件名称与共享货架中的组件不冲突
    #组件名称
    component_name="API网关组件"  --并且要保证该组件名称与共享货架中的组件不冲突
    #组件描述
    component_description="API网关组件"
    #组件标签
    component_tag=${projectName}
  ```


## 启动

### 方式一、所有组件本地启动

#### 必须启动的组件清单

|组件名称|组件工程名|端口|备注|
|--|--|--|--|
|注册中心|cbb_register_center|8761|这是在本地搭建共享组件平台的首要启动组件,用于发现各个启动的组件|
|用户,角色,菜单管理组件|cbb_userauth|22224|用于提供用户，角色，可访问功能等信息，协助网关进行共享组件访问鉴权|
|API网关|cbb_api_gateway_db|22222|网关是外部系统访问各个共享组件的唯一入口,包括外部系统API调试时的Swagger的入口,路由到各个服务,给API调用方分发token,访问鉴权|


#### 修改所有要启动的组件的配置文件

- 切换到开发环境模式配置文件，需要配置组件中的`application.yml`配置文件:
    ``` yml
    spring:
      profiles:
        active: dev --改为dev
    ```

- 修改自己开发的组件的应用程序名称(需要辨识该服务相关信息)，需要配置组件中的`application.yml`配置文件:
    ``` yml
    spring:
      application:
        name: "cbb-demo" -- 分隔符必须用'-'
    ```

- 并修改`application-dev.yml`配置文件中的:
    ``` yml
    spring:
      redis: localhost --改为localhost地址
    eureka:
      client:
        serviceUrl:
          defaultZone: "http://ucp:Summit2018@localhost:8761/eureka/" --注册中心组件默认端口是8761，所以注册中心组件无需配置，所以直接启动即可
    ```

- 根据上述组件启动清单，依次启动组件，运行`MainAction.java`

- 各个组件启动完成后访问<http://localhost:8761/admin/login>,查看各个组件运行状态，访问SpringBootAdmin的用户名和密码分别是：
    ```
      用户名:ucp
      密码:Summit2018
     ```

- 当出现如下状态，<font color=green size=5px>all up</font> 则表示组件运行正常:

  ![SpringBootAdmin监控](doc/SpringBootAdmin.png)

- 各个组件启动完成后访问<http://localhost:22222/swagger-ui.html>,这个界面来进行接口调试，并在下图红框中切换各个组件，调试各个组件的接口，如下图所示：

  ![Swagger接口调试界面](doc/Swagger-test-ui.png)

### 方式二、只启动自己所要开发的组件

#### 修改自己所要开发的组件的配置文件

- 切换到开发环境模式配置文件，需要配置组件中的`application.yml`配置文件:
    ``` yml
    spring:
      profiles:
        active: dev --改为dev
    ```

- 修改自己开发的组件的应用程序名称(需要辨识该服务相关信息)，需要配置组件中的`application.yml`配置文件:
    ``` yml
    spring:
      application:
        -- 建议name的命名格式为 你的名字的全拼+应用的名称, 例如：
        name: "liuyuan-cbb-demo" -- 分隔符必须用'-' (不能与服务器平台中已经运行的组件应用程序重名)
        
    ```
    
    <font color=red size=5px>注意：</font>上图的组件应用程序名称不能与服务器平台中已经运行的组件应用程序重名，一旦重名，平台会认为是同一个组件的多个实例，网关在调用组件接口时，Ribbon负载均衡器会根据负载均衡算法，自动路由到相同的应用程序名称的组件的其中一个，造成<font color=red size=5px>从网关过来的请求无法到达你本地开发机</font>的问题。

- 运行自己组件的`MainAction.java`文件即可。

- 当自己的组件启动完成后访问<http://192.168.140.155:45000/admin/login>,查看各个组件运行状态，访问SpringBootAdmin的用户名和密码分别是：
    ```
      用户名:ucp
      密码:Summit2018
     ```

    <font color=red size=5px>注意：</font>因为注册中心发现组件有30秒左右的延时，所以你需要稍等一会才能在界面中看到你的组件，才可以在网关Swagger中访问到你的组件。

- 当出现如下状态，有红框处的组件状态，<font color=green size=5px>all up</font> 则表示组件运行正常:

  ![SpringBootAdmin监控](doc/Swagger-test-ui-cloud.png)

- 你的组件启动完成后访问<http://192.168.140.155:45001/swagger-ui.html>,这个界面来进行接口调试，并在下图红框中切换各个组件，调试各个组件的接口，如下图所示：

  ![Swagger接口调试界面](doc/Swagger-test-ui.png)

- 或者可以访问你正在开发组件的swagger界面进行调试，在自己组件开发可以不用登陆，因为已经绕过了网关。<font color=red size=5px>但是无法获取当前登录的用户信息</font>

## 测试接口

由于使用OAuth2.0授权认证模式，所以通过网关访问接口时，需要登录认证才能访问。

### 登录

- 使用OAuth2.0的`password`模式登录

    ``` yml
    需要如下参数:

    username: admin  --登录用户名
    password: 0VlUiYNzE+SnJMcW1636jQ==  --登录用户密码(已经通过AES加密,原密码是:123456)
    client_id: summit  --请求客户端ID
    client_secret: summit  --请求客户端code
    grant_type: password   --OAuth2.0认证模式
    scope: server    --认证作用域(默认全平台有效)
    ```

    登录方式如下图:

    ![登录获取授权](doc/Auth.png)

    填完认证表单后点击`Authorize`按钮即可完成接口访问授权。

    授权成功后，会出现锁子闭合的图标，即表示接口允许访问，如下图:

    ![授权成功](doc/AuthSuccess.png)

    <font color=black size=5px>此时就可以测试接口了!</font>
    <br>

    <font color=red size=5px>注意：</font>`password`字段在<http://tool.chacuo.net/cryptaes>这个站点进行AES加密和解密，<font color=red size=5px>秘钥</font>目前使用的是`summitsummitsumm`

   加解密方式如下图：

   ![password字段加密和解密方法](doc/AESDecodeEncode.png)

## 最后

  平台最终发布的对外接口URL请以网关swagger提供的为准，所以前端和测试同学，在使用的时候只需要通过网关来访问所需要的接口即可。例如下图：

  ![测试接口的url](doc/TestApi.png)
  
  后端同学请注意，网关在对外提供各个服务组件的rest接口时，都会给不同的服务组件追加URL访问前缀，例如`/api/userauth/user/queryUserRoleByUserName`，实际上该组件的真实接口是`/user/queryUserRoleByUserName`,所以各个服务组件的开发同学，不用刻意的给你的rest接口加访问前缀，<font color=black size=5px>不用担心和其他组件的接口冲突。</font>

## 关于共享组件发布到应用货架的注意事项:

- 配置构建脚本：
  在你的组件中的`deploy/jenkins_build.sh`文件中，如图：

  ![点击新建任务](doc/deploy_script.png)

- 每个业务组件需要在`192.168.140.155:6333`这台服务器的CI系统上创建一个自己的构建任务,创建构建任务的步骤：
  - 第一步：
    ![点击新建任务](doc/create_build_job.png)
  - 第二步，复制脚手架配置，然后点击确定：
    ![复制脚手架配置](doc/copy_config.png)
  - 第三步,更换仓库地址：
    ![更换仓库地址](doc/change_git_address.png)
  - 第四步,更换脚本执行路径,然后点击保存：
    ![更换仓库地址](doc/change_build_path.png)

- 如下图创建的是一个共享组件脚手架的构建任务：

    ![一个共享组件脚手架的构建任务](doc/jenkins.png)

## 其他注意事项

- 组件获取登录的用户信息的方式如下(仅针对SpringMVC使用,并且只有通过网关登录之后访问你的组件时，才可以用该方法获取用户信息):

    ``` java
    //该方法可获取用户的基本信息，比如用户名称、密码、地址等等，详见UserInfo类(用户信息来源于redis,Key的格式为user_info_xxx,xxx代表全局唯一的用户名称)
    UserInfo userInfo = UserContextHolder.getUserInfo();
    ```

## 后台接口状态码描述

|状态码|描述消息|应用场景
|:---|:---|:---|
|CODE_0000|请求成功| 请求成功返回|
|CODE_9991|对象不存在| 用于更新操作|
|CODE_9992|对象已存在| 用于新增操作|
|CODE_9993|非法的请求参数| 用于过滤GET请求|
|CODE_9994|非法的文件类型| 用于上传文件|
|CODE_9995|文件不能为空| 用于上传文件|
|CODE_9996|文件大小超限| 用于上传文件|
|CODE_9999|服务端未知异常| 未知异常返回|
|CODE_4007|访问受限，需要token| 用于调用接口前校验token|
|CODE_4008|无效的token| 用于token失效|
|CODE_4022|用户已存在| 用于用户注册|
|CODE_4013|两次输入的密码不一致| 用于用户注册或修改密码|
|CODE_4023|用户不存在| 用于用户登录|
|CODE_4010|用户名或密码错误| 用于用户登录|
|CODE_4024|用户被锁定，请联系管理员进行解锁| 用于用户登录|
|CODE_4009|旧密码错误| 用于修改密码|
|CODE_4012|权限不足| 用于权限不足|
|CODE_4011|登录状态失效，请重新登录| 用于登录状态失效|
