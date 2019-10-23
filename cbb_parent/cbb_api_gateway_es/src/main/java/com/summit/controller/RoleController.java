package com.summit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.service.role.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * @author yt
 */
@RestController
@Api("权限模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @ApiOperation(value = "新增权限", notes = "用于application/json格式")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(@ApiParam(value = "权限信息example:{\"CODE\":\"admin\",\"NAME\":\"admin\",\"NOTE\":\"备注\"}", required = true) @RequestBody JSONObject jsonObject) {
        return roleService.addRole(jsonObject);
    }

    @ApiOperation(value = "修改权限", notes = "用于application/json格式")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Object edit(@ApiParam(value = "权限信息example:{\"CODE\":\"admin\",\"NAME\":\"admin\",\"NOTE\":\"备注\"}", required = true) @RequestBody JSONObject jsonObject) {

        return roleService.editRole(jsonObject);
    }


    @ApiOperation(value = "删除权限")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(@ApiParam(value = "权限id", required = true) @RequestParam(value = "codes") String codes) {

        return roleService.deleteRole(codes);
    }

    @ApiOperation(value = "查询所有")
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public Object queryAll() {
        return roleService.queryAll();
    }

    @ApiOperation(value = "根据code查询权限")
    @RequestMapping(value = "/queryByCode", method = RequestMethod.POST)
    public Object queryByCode(@ApiParam(value = "权限id", required = true) @RequestParam(value = "codes") String codes) {

        return roleService.queryRoleByCodes(codes);
    }


    @ApiOperation(value = "根据条件分页查询")
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Object queryPage(@ApiParam(value = "当前页", required = false) @RequestParam(name = "currentPage") Integer currentPage
            , @ApiParam(value = "每页大小", required = false) @RequestParam(name = "pageSize") Integer pageSize
            , @ApiParam(value = "跳转页数", required = false) @RequestParam(name = "willJumpPage") Integer willJumpPage
            , @ApiParam(value = "用户信息 example :{\"NAME\":\"admin\"}", required = true) @RequestBody JSONObject jsonObject
    ) {
        return roleService.queryByPage(currentPage, pageSize, willJumpPage, jsonObject);
    }


    @ApiOperation(value = "根据code查询roleFunction")
    @RequestMapping(value = "/getRoleFunInfo", method = RequestMethod.POST)
    public Object getRoleFunInfo(@ApiParam(value = "权限id", required = true) @RequestParam(value = "code") String code) {
        JSONObject js = new JSONObject();
        Map<String, Object> jsonObject = roleService.queryAll();
        JSONArray jsonList = null;
        String data = "data";
        if (jsonObject != null && jsonObject.containsKey(data)) {
            jsonList = (JSONArray) jsonObject.get(data);
        }
        js.put("treeNode", jsonList == null ? "" : jsonList);
        js.put("funId", roleService.queryFunIdByRoleCode(code));
        js.put("success", true);
        return js;
    }

    @ApiOperation(value = "roleAuthorization")
    @RequestMapping(value = "/roleAuthorization", method = RequestMethod.POST)
    public Object roleAuthorization(@ApiParam(value = "权限id", required = true) @RequestParam(value = "code") String code
            , @ApiParam(value = "functionId", required = true) @RequestParam(value = "functionId") String functionId
    ) {

        return roleService.roleAuthorization(code, functionId);
    }


    @ApiOperation(value = "将roleCode赋予多个用户")
    @RequestMapping(value = "/roleGrantUser", method = RequestMethod.POST)
    public Object roleGrantUser(@ApiParam(value = "权限id", required = true) @RequestParam(value = "code") String code
            , @ApiParam(value = "username", required = true) @RequestParam(value = "username") String username
    ) {
        return roleService.roleGrantUser(code, username);
    }


}
