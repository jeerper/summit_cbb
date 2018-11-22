### 1、菜单路由

- Dashboard
    - 分析页
    - 监控页
    - 工作台
- 表单页
    - 基础表单页
    - 分步表单页
    - 高级表单页
- 列表页
    - 查询表格
    - 标准列表
    - 卡片列表
    - 搜索列表（项目/应用/文章）
- 详情页
    - 基础详情页
    - 高级详情页
- 结果
    - 成功页
    - 失败页
- 异常
    - 403 无权限
    - 404 找不到
    - 500 服务器出错
- 个人页
    - 个人中心
    - 个人设置
- 帐户
    - 登录
    - 注册
    - 注册成功

### 2、目录结构

- assets               # 本地静态资源
- components           # 业务通用组件 index.tsx的export
- e2e                  # 集成测试用例
- layouts              # 通用布局
- pages                # 业务页面入口和常用模板
- apis             # 后台接口服务
- utils                # 工具库
- locales              # 国际化资源
- defaultSettings.ts   # 布局样式设置
- index.scss          # 全局样式
- index.tsx            # 全局主入口

### 3、布局
> 包含了导航栏、侧边栏、页脚、通知栏、内容区,
> 在路由中进行配置,
> 菜单和路由挂钩 (路由管理 -》 菜单生成 -》 面包屑)

- BasicLayout # 基础页面布局，包含头部导航，侧边栏和通知栏
- UserLayout # 登录注册的通用布局
- BlankLayout # 空白布局
- Content # 内容区
- Footer # 底部页脚
- Header # 头部导航
- Slider # 侧边栏

### 4、路由配置的字段

```json
   {
       name: 'dashborad', # 当前路由在菜单和面包屑中的名称
       icon: 'dashborad', # 当前路由在菜单下的图标名
       path: '/app', # 路由地址
       component: 'BasicLayoutComponent', # 关联组件
       redirect: '/', # 未匹配重定向地址
       routes: [ # 子路由
           {
               path: '/user', # 路由地址
               redirect: '/user', # 未匹配重定向地址
               component: 'UserComponent' # 关联组件
           }
       ]
       hideInMenu: true, # 当前路由在菜单中不展现，默认 false
       hideChildrenInMenu: true, # 当前路由的子级在菜单中不展现，默认 false
       hideInBreadcrumb: true, # 当前路由在面包屑中不展现，默认 false
       authority: ['admin'] # 允许展示的权限，不设则都可见
   }

```


