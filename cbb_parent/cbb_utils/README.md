## cbb_utils
> 共享工具类包

### EXCEL 批量导入导出注解 @ExcelField   

> 实体类需要导入或导出的字段必须加@ExcelField(title="用户名", sort = 1, align = 2)，相关配置详见@ExcelField的注释。   

- 1、批量导入   

```
	@RequestMapping(value="/import", method = RequestMethod.POST)
	@ApiOperation(notes="批量导入", value="批量导入")
	public RestfulEntityBySummit<?> importExcel(
			@ApiParam(value = "上传的文件")@RequestParam("importFile") MultipartFile file) {
		try {
			ImportExcel ie = new ImportExcel(file, tableHeadNum, sheetIndex);
			ReadExcelResult<T> result = ie.getDataList(T.class);
			// 判断是否有错误提示
			if(result.getErrorMsg().length() > 0){
				// 处理有非法数据的逻辑
			}
			// 获取数据记录
			List<T> list = result.getDataList();
			// 执行导入逻辑
		} catch (InvalidFormatException | IOException | InstantiationException | IllegalAccessException e) {
//			e.printStackTrace();
//			处理异常
		}
		return ResultBuilder.buildSuccess();
	}
```   
> <font color="red">备注：tableHeadNum为表头行号（从0开始），sheetIndex为sheet页的下标（从0开始），T为实体类型</font>   

- 2 批量导出或模板下载   

```
	@RequestMapping(value="/export", method = RequestMethod.GET)
	@ApiOperation(notes="批量导出或下载模板", value="批量导出或下载模板")
	public void export(
			HttpServletResponse resp) {
		// 查询数据
		List<User> users = userService.findAll();
		String title = "用户清单";
		String note = "包含所有用户";
		String fileName = "系统用户清单.xlsx";
		try {
			new ExportExcel(title, note, User.class).setDataList(users).write(resp, fileName).dispose();
		} catch (IOException e) {
//			e.printStackTrace();
//			处理异常
		}
	}
```