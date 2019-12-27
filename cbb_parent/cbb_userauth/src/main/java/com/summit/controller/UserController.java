package com.summit.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summit.MainAction;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.*;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserService;
import com.summit.util.DateUtil;
import com.summit.util.PermissionUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Api(description = "用户管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    LogUtilImpl logUtil;
    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    private UserService us;
    @Value("${password.encode.key}")
    private String key;

    @PostMapping("/add")
    @ApiOperation(value = "新增用户,可以上传base64格式的头像", notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    public RestfulEntityBySummit<String> add(@RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        try {

            String base64Str = userInfo.getHeadPortrait();
            if (SummitTools.stringNotNull(base64Str)) {
                StringBuffer fileName = new StringBuffer();
                fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
                if (base64Str.indexOf("data:image/png;") != -1) {
                    base64Str = base64Str.replace("data:image/png;base64,", "");
                    fileName.append(".png");
                } else if (base64Str.indexOf("data:image/jpeg;") != -1) {
                    base64Str = base64Str.replace("data:image/jpeg;base64,", "");
                    fileName.append(".jpeg");
                }
                String picId = IdWorker.getIdStr();
                String headPicpath = new StringBuilder()
                        .append(SystemUtil.getUserInfo().getCurrentDir())
                        .append(File.separator)
                        .append(MainAction.SnapshotFileName)
                        .append(File.separator)
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();
                String headPicUrl = new StringBuilder()
                        .append("/")
                        .append(MainAction.SnapshotFileName)
                        .append("/")
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();

                byte[] fileBytes = null;
                try {
                    fileBytes = Base64.getDecoder().decode(base64Str);
                } catch (Exception e) {
                    log.error("图片base64格式有误!");
                    userInfo.setHeadPortrait("图片base64格式有误!");
                }
                FileUtil.writeBytes(fileBytes, headPicpath);
                userInfo.setHeadPortrait(headPicUrl);
            }

            logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
            ResponseCodeEnum c = us.add(userInfo, key);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(), userInfo);
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logger.error("新增用户失败", e);
        }
        userInfo.setPassword(null);
        SummitTools.getLogBean(logBean, "用户管理", "新增用户信息：" + JSONObject.fromObject(userInfo).toString(), "1");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }

    }


    @ApiOperation(value = "新增用户---以附件的形式头像上传", notes = "昵称(name),用户名(userName),密码(password)都是必输项")
    @RequestMapping(value = "/addUserinfo", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RestfulEntityBySummit<String> addUserinfo(@ApiParam(value = "用户头像", allowMultiple = true) MultipartFile[] headPortrait,
                                                     UserInfo userInfo
    ) {
        LogBean logBean = new LogBean();
        try {
            if (headPortrait != null && headPortrait.length > 0) {
                String picId = IdWorker.getIdStr();
                String headPicpath = new StringBuilder()
                        .append(SystemUtil.getUserInfo().getCurrentDir())
                        .append(File.separator)
                        .append(MainAction.SnapshotFileName)
                        .append(File.separator)
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();
                String headPicUrl = new StringBuilder()
                        .append("/")
                        .append(MainAction.SnapshotFileName)
                        .append("/")
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();
                FileUtil.writeBytes(headPortrait[0].getBytes(), headPicpath);
                userInfo.setHeadPortrait(headPicUrl);
            }

            logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
            ResponseCodeEnum c = us.add(userInfo, key);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(), userInfo);
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logger.error("新增用户失败", e);
        }
        userInfo.setPassword(null);
        SummitTools.getLogBean(logBean, "用户管理", "新增用户信息：" + JSONObject.fromObject(userInfo).toString(), "1");
        // logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }

    }

    /**
     * 此处的删除只修改状态。
     *
     * @param userNames
     * @return
     */
    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public RestfulEntityBySummit<String> del(
            @RequestParam(value = "userNames") String userNames) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            if (!permissionUtil.checkLoginUserAccessPermissionToOtherUser(userNames)) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4012);
            }
            if (userNames.contains(",")) {
                for (String username : userNames.split(",")) {
                    //系统管路员用户不能删除
                    if (SummitTools.stringEquals(SysConstants.SUPER_USERNAME, username)) {
                        continue;
                    }
                    userInfoCache.deleteUserInfo(username);
                }
            }
            us.del(userNames);
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("删除用户信息", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "用户管理", "删除用户:" + userNames, "3");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }

    }

    @ApiOperation(value = "修改用户", notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    @PostMapping("/edit")
    public RestfulEntityBySummit<String> edit(@RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            if (!permissionUtil.checkLoginUserAccessPermissionToOtherUser(userInfo.getUserName())) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4012);
            }
            String base64Str = userInfo.getHeadPortrait();
            if (SummitTools.stringNotNull(base64Str)) {
                StringBuffer fileName = new StringBuffer();
                fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
                if (base64Str.indexOf("data:image/png;") != -1) {
                    base64Str = base64Str.replace("data:image/png;base64,", "");
                    fileName.append(".png");
                } else if (base64Str.indexOf("data:image/jpeg;") != -1) {
                    base64Str = base64Str.replace("data:image/jpeg;base64,", "");
                    fileName.append(".jpeg");
                }
                String picId = IdWorker.getIdStr();
                String headPicpath = new StringBuilder()
                        .append(SystemUtil.getUserInfo().getCurrentDir())
                        .append(File.separator)
                        .append(MainAction.SnapshotFileName)
                        .append(File.separator)
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();
                String headPicUrl = new StringBuilder()
                        .append("/")
                        .append(MainAction.SnapshotFileName)
                        .append("/")
                        .append(picId)
                        .append("_Head.jpg")
                        .toString();

                byte[] fileBytes = null;
                try {
                    fileBytes = Base64.getDecoder().decode(base64Str);
                } catch (Exception e) {
                    log.error("图片base64格式有误!");
                    userInfo.setHeadPortrait("图片base64格式有误!");
                }
                FileUtil.writeBytes(fileBytes, headPicpath);
                userInfo.setHeadPortrait(headPicUrl);
            }
            ResponseCodeEnum c = us.edit(userInfo, key);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }


            UserInfo cacheUserInfo = userInfoCache.getUserInfo(userInfo.getUserName());
            if (cacheUserInfo != null) {
                BeanUtil.copyProperties(userInfo, cacheUserInfo, CopyOptions.create().setIgnoreNullValue(true));
                userInfoCache.setUserInfo(userInfo.getUserName(), cacheUserInfo);
            }
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改用户失败:", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "用户管理", "修改用户:" + JSONObject.fromObject(userInfo).toString(), "2");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }


    @ApiOperation(value = "修改密码", notes = "旧密码和新密码必须是加密后的数据")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<String> editPassword(@RequestBody UserPassWordInfo userPassWordInfo) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            if (!permissionUtil.checkLoginUserAccessPermissionToOtherUser(userPassWordInfo.getUserName())) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4012);
            }
            if (!userPassWordInfo.getPassword().equals(userPassWordInfo.getRepeatPassword())) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4013);
            }
            ResponseCodeEnum ub = us.editPassword(userPassWordInfo.getUserName(), userPassWordInfo.getOldPassword(), userPassWordInfo.getPassword()
                    , userPassWordInfo.getRepeatPassword(), key);
            if (ub != null) {
                return ResultBuilder.buildError(ub);
            }
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改密码失败:", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "用户管理", "修改密码:" + JSONObject.fromObject(userPassWordInfo).toString(), "2");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }

    // public ResponseCodeEnum editImei(String imei,String username)


    @ApiOperation(value = "根据用户名修改移动设备识别码")
    @PutMapping("/editImei")
    public RestfulEntityBySummit<String> editImei(@RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            ResponseCodeEnum ub = us.editImei(userInfo.getUserName(), userInfo.getImei());
            if (ub != null) {
                return ResultBuilder.buildError(ub);
            }
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改移动设备识别码失败:", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "用户管理", "根据用户名修改移动设备识别码:" + userInfo.getImei(), "2");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息(对内接口),该接口只供注册中心使用")
    @GetMapping("/queryUserInfoByUserNameService")
    public RestfulEntityBySummit<UserInfo> queryUserInfoByUserNameService(
            @RequestParam(value = "userName") String userName) {
        try {
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<String> roleList = us.queryRoleByUserName(userName);
            List<String> funList = us.getFunByUserName(userName);

            if (roleList != null && roleList.size() > 0) {
                String[] roleArray = new String[roleList.size()];
                roleList.toArray(roleArray);
                ub.setRoles(roleArray);
            }
            if (funList != null && funList.size() > 0) {
                String[] funArray = new String[funList.size()];
                funList.toArray(funArray);
                ub.setPermissions(funArray);
            }

            JSONObject objcet = us.queryAdcdByUserName(userName);

            if (objcet != null) {
                String[] adcdsArray = JSON.parseObject(objcet.get("adcds").toString(), new TypeReference<String[]>() {
                });
                ub.setAdcds(adcdsArray);
                String adnms = objcet.getString("adnms");
                ub.setAdnms(adnms);
            }

            JSONObject objcetdept = us.queryDeptByUserName(userName);
            if (objcetdept != null) {
                String[] deptsArray = JSON.parseObject(objcetdept.get("deptIds").toString(), new TypeReference<String[]>() {
                });
                ub.setDepts(deptsArray);
                String deptnames = objcetdept.getString("deptnames");
                ub.setDeptNames(deptnames);
            }
            return ResultBuilder.buildSuccess(ub);
        } catch (Exception e) {
            logger.error("根据用户名查询用户信息失败-对内接口：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息--对外接口")
    @GetMapping("/queryUserInfoByUserName")
    public RestfulEntityBySummit<UserInfo> queryUserInfoByUserName(@RequestParam(value = "userName") String userName) {
        try {
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            ub.setPassword(null);

            if (!permissionUtil.checkLoginUserAccessPermissionToOtherUser(userName)) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4012);
            }

            List<String> roleList = us.queryRoleByUserName(userName);
            List<String> funList = us.getFunByUserName(userName);

            if (roleList != null && roleList.size() > 0) {
                String[] roleArray = new String[roleList.size()];
                roleList.toArray(roleArray);
                ub.setRoles(roleArray);
            }
            if (funList != null && funList.size() > 0) {
                String[] funArray = new String[funList.size()];
                funList.toArray(funArray);
                ub.setPermissions(funArray);
            }
            JSONObject objcet = us.queryAdcdByUserName(userName);

            if (objcet != null) {
                String[] adcdsArray = JSON.parseObject(objcet.get("adcds").toString(), new TypeReference<String[]>() {
                });
                ub.setAdcds(adcdsArray);
                String adnms = objcet.getString("adnms");
                ub.setAdnms(adnms);
            }

            JSONObject objcetdept = us.queryDeptByUserName(userName);
            if (objcetdept != null) {
                String[] deptsArray = JSON.parseObject(objcetdept.get("deptIds").toString(), new TypeReference<String[]>() {
                });
                ub.setDepts(deptsArray);
                String deptnames = objcetdept.getString("deptnames");
                ub.setDeptNames(deptnames);
            }

            return ResultBuilder.buildSuccess(ub);
        } catch (Exception e) {
            logger.error("根据用户名查询用户信息失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "根据用户名查询权限菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<List<FunctionBean>> queryFunctionInfoByUserName(
            @RequestParam(value = "userName") String userName) {
        try {

            UserInfo ub = UserContextHolder.getUserInfo();
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            boolean isSuroleCode = false;
            if (ub.getRoles() != null && ub.getRoles().length > 0) {
                isSuroleCode = Arrays.asList(ub.getRoles()).contains(SysConstants.SUROLE_CODE);
            }
            List<FunctionBean> funList = us.getFunInfoByUserName(userName, isSuroleCode);

            return ResultBuilder.buildSuccess(funList);

        } catch (Exception e) {
            logger.error("根据用户名查询所有菜单失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据条件查询用户信息不分页")
    @GetMapping("/queryAllUser")
    public RestfulEntityBySummit<List<UserInfo>> queryAllUser(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "isEnabled", required = false) String isEnabled,
            @RequestParam(value = "state", required = false) String state) {
        try {
            JSONObject paramJson = new JSONObject();
            if (!SummitTools.stringIsNull(name)) {
                paramJson.put("name", name);
            }
            if (!SummitTools.stringIsNull(userName)) {
                paramJson.put("userName", userName);
            }
            if (!SummitTools.stringIsNull(isEnabled)) {
                paramJson.put("isEnabled", isEnabled);
            }
            if (!SummitTools.stringIsNull(state)) {
                paramJson.put("state", state);
            }
            List<UserInfo> pageList = us.queryUserInfoList(paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "用户分页查询")
    @GetMapping("/queryByPage")
    public RestfulEntityBySummit<Page<UserInfo>> queryByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "isEnabled", required = false) String isEnabled,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "adcd", required = false) String adcd,
            @RequestParam(value = "deptName", required = false) String deptName,
            @RequestParam(value = "deptId", required = false) String deptId,
            @RequestParam(value = "sortColumn", required = false) String sortColumn,
            @RequestParam(value = "sortName", required = false) String sortName) {
        try {
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;

            JSONObject paramJson = new JSONObject();
            if (!SummitTools.stringIsNull(name)) {
                paramJson.put("name", name);
            }
            if (!SummitTools.stringIsNull(userName)) {
                paramJson.put("userName", userName);
            }
            if (!SummitTools.stringIsNull(isEnabled)) {
                paramJson.put("isEnabled", isEnabled);
            }
            if (!SummitTools.stringIsNull(state)) {
                paramJson.put("state", state);
            }
            if (!SummitTools.stringIsNull(adcd)) {
                paramJson.put("adcd", adcd);
            }
            if (!SummitTools.stringIsNull(deptId)) {
                paramJson.put("deptId", deptId);
            }
            if (!SummitTools.stringIsNull(phone)) {
                paramJson.put("phone", phone);
            }
            if (!SummitTools.stringIsNull(sortColumn)) {
                paramJson.put("sortColumn", sortColumn);
            }
            if (!SummitTools.stringIsNull(sortName)) {
                paramJson.put("sortName", sortName);
            }

            Page<UserInfo> pageList = us.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "重置密码,只需输入UserName和Password", notes = "重置的密码必须是加密后的数据")
    @PutMapping("/resetPassword")
    public RestfulEntityBySummit<String> resetPassword(
            @RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            us.resetPassword(userInfo.getUserName(), userInfo.getPassword(), key);
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("重置密码失败：", e);
            logBean.setActionFlag("0");
        }
        SummitTools.getLogBean(logBean, "用户管理", "重置密码:用户名 " + userInfo.getUserName(), "2");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleByUserName(
            @RequestParam(value = "userName") String userName) {
        try {
            UserInfo ub = UserContextHolder.getUserInfo();
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<String> list = us.queryRoleByUserName(userName);
            return ResultBuilder.buildSuccess(list);
        } catch (Exception e) {
            logger.error("根据用户名查询角色失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询角色--基于antd：Transfer穿梭框")
    @GetMapping("/queryRoleListAntdByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleListAntdByUserName(
            @RequestParam(value = "userName") String userName) {
        try {
            UserInfo ub = UserContextHolder.getUserInfo();
            ;
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<String> list = us.queryRoleListByUserName(userName);
            return ResultBuilder.buildSuccess(list);
        } catch (Exception e) {
            logger.error("根据用户名查询角色失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "授权权限，只需输入userName和roles")
    @PutMapping("/grantRole")
    public RestfulEntityBySummit<String> grantRole(
            @RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            UserInfo ub = UserContextHolder.getUserInfo();
            if (ub == null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            us.grantRole(userInfo.getUserName(), userInfo.getRoles());
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logger.error("授权权限失败：", e);
            // return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
        SummitTools.getLogBean(logBean, "用户管理", "授权权限：" + userInfo.getUserName() + ",角色信息:" + userInfo.getRoles(), "4");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }

    @ApiOperation(value = "新增时根据部门id查询部门专员职位是否存在")
    @GetMapping("/queryDutyByDpetId")
    public RestfulEntityBySummit<List<String>> queryDutyByDpetId(@RequestParam(value = "duty") String duty,
            @RequestParam(value = "deptId") String deptId) {
        try {
            if (SummitTools.stringNotNull(duty) && "3".equals(duty)){
                boolean flag=false;
                if (SummitTools.stringNotNull(deptId)){
                    List<UserDeptDutyBean> list = us.queryDutyByDpetId(deptId);
                    for (UserDeptDutyBean userDeptDutyBean:list){
                        String duty1 = userDeptDutyBean.getDuty();
                        if ("3".equals(duty1)){
                            flag=true;
                            break;
                        }
                    }
                }
                if (flag){
                    return ResultBuilder.buildError(ResponseCodeEnum.CODE_9992);
                }
            }
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000);
        } catch (Exception e) {
            logger.error("根据部门id查询部门专员职位是否存在失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "编辑时根据部门id查询部门专员职位是否存在")
    @GetMapping("/editUserQueryDutyByDpetId")
    public RestfulEntityBySummit<List<String>> editUserQueryDutyByDpetId(@RequestParam(value = "duty") String duty,
                                                                 @RequestParam(value = "deptId") String deptId,
                                                                 @RequestParam(value = "username")String username) {
        try {
            boolean flag=false;
            if (SummitTools.stringNotNull(username) && SummitTools.stringNotNull(duty)){
                UserDeptDutyBean userDeptDutyBean= us.editUserQueryDutyByDpetId(username);
                if (!StrUtil.isEmpty(userDeptDutyBean.getDuty()) && userDeptDutyBean.getDuty().equals("3") && duty.equals("3")){
                    return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000);
                }
                if (SummitTools.stringNotNull(duty) && "3".equals(duty)){
                    if (SummitTools.stringNotNull(deptId)){
                        List<UserDeptDutyBean> list = us.queryDutyByDpetId(deptId);
                        for (UserDeptDutyBean udd:list){
                            String duty1 = udd.getDuty();
                            if ("3".equals(duty1)){
                                flag=true;
                                break;
                            }
                        }
                    }
                }
            }
            if (flag){
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9992);
            }
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000);
        } catch (Exception e) {
            logger.error("根据部门id查询部门专员职位是否存在失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
}
