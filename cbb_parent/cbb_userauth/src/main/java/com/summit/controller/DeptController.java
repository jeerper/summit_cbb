package com.summit.controller;

import java.util.Date;
import java.util.List;

import com.summit.common.entity.*;
import com.summit.util.EditInvalidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.summit.cbb.utils.page.Page;
import com.summit.common.util.ResultBuilder;
import com.summit.service.dept.DeptService;
import com.summit.service.log.LogUtilImpl;
import com.summit.util.DateUtil;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 部门信息管理
 *
 * @author liuyh
 */
@Api(description = "部门管理")
@RestController  /* @Controller + @ResponseBody*/
@RequestMapping("dept")
public class DeptController {
    private static final Logger logger = LoggerFactory.getLogger(DeptController.class);
    @Autowired
    private DeptService ds;
    @Autowired
    LogUtilImpl logUtil;
    @Autowired
    EditInvalidUtil editInvalidUtil;
    /**
     * 查询部门树
     *
     * @return
     */
    @ApiOperation(value = "查询部门树")
    @RequestMapping(value = "/queryTree", method = RequestMethod.GET)
    public RestfulEntityBySummit<DeptBean> queryTree(@RequestParam(value = "pid", required = false) String pid) {
        try {
            return ResultBuilder.buildSuccess(ds.queryDeptTree(pid));
        } catch (Exception e) {
            logger.error("查询部门树失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 查询部门树(加数据权限）
     *
     * @return
     */
    @ApiOperation(value = "查询部门树(加数据权限)")
    @RequestMapping(value = "/queryTreeAuth", method = RequestMethod.GET)
    public RestfulEntityBySummit<DeptBean> queryTreeAuth(@RequestParam(value = "pid", required = false) String pid) {
        try {
            return ResultBuilder.buildSuccess(ds.queryTreeAuth(pid));
        } catch (Exception e) {
            logger.error("查询部门树失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "查询部门--JSON 数据直接生成树结构", notes = "用于application/json格式")
    @GetMapping(value = "/queryDeptJsonTree")
    public RestfulEntityBySummit<DeptTreeBean> queryJsonTree(@RequestParam(value = "pid", required = false) String pid
    ) {
        try {
            DeptTreeBean adcdBean = ds.queryJsonAdcdTree(pid);
            return ResultBuilder.buildSuccess(adcdBean);
        } catch (Exception e) {
            logger.error("数据查询失败！", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 根据id查询（分页）
     */
    @ApiOperation(value = "根据id查询")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public RestfulEntityBySummit<DeptBean> queryById(@RequestParam(value = "id") String id) {
        try {
            return ResultBuilder.buildSuccess(ds.queryById(id));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 根据id查询（分页）
     */
    @ApiOperation(value = "根据id查询部门包含行政区划")
    @RequestMapping(value = "/queryDeptAdcdById", method = RequestMethod.GET)
    public RestfulEntityBySummit<DeptBean> queryDeptAdcdById(@RequestParam(value = "id") String id) {
        try {
            return ResultBuilder.buildSuccess(ds.queryDeptAdcdById(id));
        } catch (Exception e) {
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }

        //return list;
    }

    @ApiOperation(value = "根据adcd查询部门信息")
    @RequestMapping(value = "/queryDeptByAdcd", method = RequestMethod.GET)
    public RestfulEntityBySummit<List<DeptBean>> queryDeptByAdcd(@RequestParam(value = "adcd", required = false) String adcd) {
        try {
            return ResultBuilder.buildSuccess(ds.queryDeptByAdcd(adcd));
        } catch (Exception e) {
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "查询所有的部门信息")
    @RequestMapping(value = "/queryAllDept", method = RequestMethod.GET)
    public RestfulEntityBySummit<List<DeptBean>> queryAllDept() {
        try {
            return ResultBuilder.buildSuccess(ds.queryAllDept());
        } catch (Exception e) {
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "根据pid查询分页")
    @RequestMapping(value = "/queryByPidPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<DeptBean>> queryByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "pid", required = false) String pid,
            @RequestParam(value = "deptcode", required = false) String deptcode,
            @RequestParam(value = "deptname", required = false) String deptname,
            @RequestParam(value = "adcd", required = false) String adcd,
            @RequestParam(value = "adnm", required = false) String adnm) {
        //Page<JSONObject> list = null;
        try {
            JSONObject paramJson = new JSONObject();
            paramJson.put("pid", pid);
            paramJson.put("deptcode", deptcode);
            paramJson.put("deptname", deptname);
            paramJson.put("adcd", adcd);
            paramJson.put("adnm", adnm);
            return ResultBuilder.buildSuccess(ds.queryByPage(page, pageSize, paramJson));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "部门分页查询加部门权限(即：当前登录人只能查询他所属部门以及子部门下的部门分页数据)")
    @RequestMapping(value = "/queryDeptByPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<DeptBean>> queryDeptByPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "deptcode", required = false) String deptcode,
            @RequestParam(value = "deptname", required = false) String deptname,
            @RequestParam(value = "adcd", required = false) String adcd,
            @RequestParam(value = "adnm", required = false) String adnm,
            @RequestParam(value = "pid", required = false) String pid){
        try{
            JSONObject paramJson = new JSONObject();
            paramJson.put("deptcode", deptcode);
            paramJson.put("deptname", deptname);
            paramJson.put("adcd", adcd);
            paramJson.put("adnm", adnm);
            paramJson.put("pid",pid);
            return ResultBuilder.buildSuccess(ds.queryDeptByPage(page, pageSize, paramJson));
        }catch (Exception e){
            logger.error("查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }

    }

    /**
     * 新增
     */
    @ApiOperation(value = "部门新增", notes = "部门编码（deptCode）不能重复添加;部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid=-1 ")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RestfulEntityBySummit<String> add(@RequestBody DeptBean deptBean) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        SummitTools.getLogBean(logBean, "部门管理", "新增部门信息:" + JSONObject.fromObject(deptBean).toString(), "1");
        try {
            ResponseCodeEnum responseCodeEnum = ds.add(deptBean);
            if (responseCodeEnum != null) {
                return ResultBuilder.buildError(responseCodeEnum);
            } else {
                logBean.setActionFlag("1");
                logUtil.insertLog(logBean);
                return ResultBuilder.buildSuccess();
            }
        } catch (Exception e) {
            logger.error("操作失败：", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logUtil.insertLog(logBean);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 编辑保存
     */
    @ApiOperation(value = "部门编辑", notes = "id,部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid=-1 ")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestfulEntityBySummit<String> edit(@RequestBody DeptBean deptBean) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        SummitTools.getLogBean(logBean, "部门管理", "修改部门信息:" + JSONObject.fromObject(deptBean).toString(), "2");
        try {
            ds.edit(deptBean);
            logBean.setActionFlag("1");
            logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("操作失败：", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logUtil.insertLog(logBean);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "修改部门审批", notes = "id,部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid=-1")
    @PostMapping("/editAudit")
    public RestfulEntityBySummit<String> editAudit(@RequestBody DeptAuditBean deptAuditBean){
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        SummitTools.getLogBean(logBean, "部门审核管理", "修改部门信息:" + JSONObject.fromObject(deptAuditBean).toString(), "2");
        try {
            boolean b=editInvalidUtil.editDeptInvalid(deptAuditBean);
            if (b){
                logger.error("无效的编辑");
                return ResultBuilder.buildSuccess();
            }
            ResponseCodeEnum c =ds.editAudit(deptAuditBean);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }
            logBean.setActionFlag("1");
            logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        }catch (Exception e){
            logger.error("操作失败：", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logUtil.insertLog(logBean);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    /**
     * 删除
     */
    @ApiOperation(value = "部门删除，删除多个以,分割")
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public RestfulEntityBySummit<String> del(@RequestParam(value = "ids") String ids) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        SummitTools.getLogBean(logBean, "部门管理", "删除部门信息:" + ids, "3");
        try {
            ResponseCodeEnum responseCodeEnum = ds.del(ids);
            if (responseCodeEnum != null) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999, "有子集信息，无法删除", null);
            }
            logBean.setActionFlag("1");
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("操作失败：", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logUtil.insertLog(logBean);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999, e.getMessage(), null);
        }
    }


    @ApiOperation(value = "根据pdept查询下面所有的子节点(四级)")
    @RequestMapping(value = "/queryAllDeptByPdept", method = RequestMethod.GET)
    public RestfulEntityBySummit<List<String>> queryAllDeptByPdept(
            @RequestParam(value = "pdept", required = true) String pdept){
        try {
            return ResultBuilder.buildSuccess(ds.getDeptBean(pdept));
        } catch (Exception e) {
            logger.error("数据查询失败！", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "根据pdept查询下面所有的子节点(不包括父节点、多级)")
    @RequestMapping(value = "/queryLowerAllDeptByPdept", method = RequestMethod.GET)
    public RestfulEntityBySummit<List<String>> queryLowerAllDeptByPid(
            @RequestParam(value = "pdept", required = true) String pdept){
        try {
            return ResultBuilder.buildSuccess(ds.queryLowerAllDeptByPdept(pdept));
        } catch (Exception e) {
            logger.error("数据查询失败！", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据当前节点查询当前节点以上所有的父节点(包括不当前节点、多级)")
    @RequestMapping(value = "/queryParentAllDeptByPdept", method = RequestMethod.GET)
    public RestfulEntityBySummit<List<String>> queryParentAllDeptByPdept(
            @RequestParam(value = "pdept", required = true) String pdept){
        try {
            return ResultBuilder.buildSuccess(ds.queryParentAllDeptByPdept(pdept));
        } catch (Exception e) {
            logger.error("数据查询失败！", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


}
