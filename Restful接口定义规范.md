# RESTful 接口定义规范
## 前提   
> RESTful API 统一约束客户端和服务器之间的接口。简化和分离系统架构，使每个模块独立。   
- 请求中使用URI定位资源   
- 用HTTP Verbs[动词]（GET、POST、PUT、DELETE）描述操作（具体表现形式）   
- 数据传递（默认）采用：Content-Type: application/json; charset=utf-8   

## URL规范   
> 不能使用大写，用中横线 "-" 不用下划线 "_",每个资源使用两个URL,资源集合用一个URL，具体某个资源用一个URL：

```
/users         #资源集合的URL
/users/56      #具体某个资源的URL
```   
> 用名词代替动词表示资源

```
  不要这么设计                     更好的设计
/getAllUsers                      GET  /users
/getAllLockedUsers                GET  /users?state=Locked
/createUser                       POST /users
/updateUser                       PUT  /users/56
```   

## 用HTTP方法操作资源   
>使用URL指定要用的资源。使用HTTP方法来指定怎么处理这个资源。使用四种HTTP方法POST，GET，PUT，DELETE可以提供CRUD功能（创建，获取，更新，删除）   
- <b>获取：</b> 使用GET方法获取资源;GET请求从不改变资源的状态;无副作用;GET方法是幂等的;GET方法具有只读的含义;因此，你可以完美的使用缓存;     
- <b>创建：</b> 使用POST创建新的资源;   
- <b>更新：</b> 使用PUT更新现有资源;  
- <b>删除：</b> 使用DELETE删除现有资源;   


&nbsp;| POST(创建)|GET(读取)|PUT(更新)|DELETE(删除)
---|---|---|---|---|---   
/users|创建用户（INSERT）|列出所有用户（SELECT）|批量更新用户信息(UPDATE)|删除用户（DELETE）
/users/56|&nbsp;|获取id为56的用户|更新id为56的用户|删除id为56的用户   

## 接口定义   

<b>Controller :</b>   
&emsp; @RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)   
#### 方法声明
<b>GET :</b>    
&emsp;@RequestMapping(value = "/56", method = RequestMethod.GET)   
&emsp;或   
&emsp;@GetMapping(value="/56")

<b>POST :</b>    
&emsp;@RequestMapping(value = "/", method = RequestMethod.POST)      
&emsp;或   
&emsp;@PostMapping(value="/")   

<b>PUT :</b>    
&emsp;@RequestMapping(value = "/56", method = RequestMethod.PUT)      
&emsp;或   
&emsp;@PutMapping(value="/56")   

<b>DELETE :</b>    
&emsp;@RequestMapping(value = "/56", method = RequestMethod.DELETE)   
&emsp;或      
&emsp;@DeleteMapping(value="/56")   

#### 参数接收   
- <font color="red"><b>URL 传参: &emsp; 使用@ApiParam注解进行参数说明</b></font> 
> <font color="red"><b>例1、</b></font>@ApiParam(value = "用户名", required = true, example = "example")@PathVariable String userName   
> <font color="red"><b>例2、</b></font>@ApiParam(value = "用户名", required = true, example = "example")@PathVariable String userName   

- <font color="red"><b>RequestBody:&emsp;使用@ApiModelProperty对对象关键字段进行说明</b></font>   
> <font color="red"><b>例：</b></font>@ApiModelProperty(value = "用户名", dataType = "String", required = true, example = "exampleuser")

## Response   


```   
    @ResponseBody
	@RequestMapping(value="", method = RequestMethod.POST)
	@ApiOperation(notes="用户注册", value="用户注册")
	public RestfulEntityBySummit<String> regist(
			@RequestBody RegistInfo user) {
//		return new RestfulEntityBySummit<String>("Demo项目运行成功，666！");// 旧版
//		return ResultBuilder.buildSuccess();// 成功，无数据返回
//		return ResultBuilder.buildSuccess("注册成功！");// 成功，有数据返回
		return ResultBuilder.buildError(ResponseCodeEnum.CODE_4022);// 用户已存在，提示消息
		
	}
```
> <font color="red">RestfulEntityBySummit<?></font> 定义了返回的结果的结构，? 为对应的数据类型，可以为 String、VO、List<VO>、Page<VO> (VO为视图层模型)；   

> 正常情况下无数据返回为：<font color="red">return ResultBuilder.buildSuccess()</font>；   

> 正常情况下返回为：<font color="red">return ResultBuilder.buildSuccess(Data)；<b>强制要求：Data中的关键字段必须加上&nbsp;@ApiModelProperty(value="说明")</b></font>;   

> 出现异常返回为：<font color="red">return ResultBuilder.buildError(ResponseCodeEnum);</font>;

> <font color="red"><b>优先使用ResponseCodeEnum中提供的通用状态码，如果需要自定义异常码和描述消息，参考http状态码,不可与通用状态码重复，错误描述必须简洁准确,具体实现如下：</b></font>   

```   
public enum ResponseCodeEnum implements ResponseCode {

	CODE_6666("猝不及防，网络异常");

	private String message;// 提示消息

	private ResponseCodeEnum(String message) {
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getCode() {
		return this.name();
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
```

2xx：成功|3xx：重定向|4xx：客户端错误|5xx：服务器错误
---|---|---|---
200 成功|301 永久重定向|400 错误请求|500 内部服务器错误
201 创建|304 资源未修改|401 未授权|
||403 禁止|
||404 未找到|