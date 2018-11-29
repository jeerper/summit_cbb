package com.summit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.config.JdbcClientImpl;
import com.summit.domain.SummitClient;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author yt
 *
 */
@RestController
@Api("client模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/client")
public class ClientController {

	@Autowired
	SummitTools st;

	@Autowired
	JdbcClientImpl jdbcClient;
	
	@ApiOperation(value = "新增client", notes = "用于application/json格式")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(@RequestBody SummitClient client) {
		System.out.println("abcdefg");
		jdbcClient.addClientDetails(client);
		return st.success("新增成功", "");
	}

	@ApiOperation(value = "修改client", notes = "用于application/json格式")
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Object edit(@RequestBody SummitClient client) {
		jdbcClient.updateClientDetails(client);
		return st.success("更新成功", "");
	}

	@ApiOperation(value = "删除client")
	@RequestMapping(value = "/{clientId}", method = RequestMethod.DELETE)
	public Object delete(
			@ApiParam(value = "clientId", required = true) @PathVariable(name = "clientId") String clientId) {
		jdbcClient.removeClientDetails(clientId);
		return st.success("删除成功", "");
	}

	@ApiOperation(value = "修改client密码")
	@RequestMapping(value = "/editPwd", method = RequestMethod.PUT)
	public Object editPwd(
			@ApiParam(value = "clientId", required = true) @RequestParam(name = "clientId",required=true) String clientId,
			@ApiParam(value = "secret", required = true) @RequestParam(name = "secret",required=true) String secret) {
		jdbcClient.updateClientSecret(clientId, secret);
		return st.success("更新成功", "");
	}

	@ApiOperation(value = "查询所有")
	@RequestMapping(value = "/queryAll", method = RequestMethod.POST)
	public Object queryAll() {

		List<ClientDetails> clientList = jdbcClient.listClientDetails();

		//System.out.println(clientList);
		return st.success("查询成功", clientList);
	}

	@ApiOperation(value = "根据查询id查询client")
	@RequestMapping(value = "/{clientId}", method = RequestMethod.POST)
	public Object query(
			@ApiParam(value = "clientId", required = true) @PathVariable(name = "clientId") String clientId) {
		ClientDetails clientDetail = jdbcClient.loadClientByClientId(clientId);
		return st.success("查询成功", clientDetail);
	}
	
	
}
