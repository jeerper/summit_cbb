package com.summit.send.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONUtil {

	//将obj转成json
	public static String parseObjToJson(Object obj){
		return JSON.toJSONString(obj);
	}


	//获取附件名称和路径
	public static List<String> getToEmails(String toEmails){
		List<String> nameAndPaths = new ArrayList<String>();
		JSONArray jsonArray = null;
		try {
			jsonArray = JSON.parseArray(toEmails);
			for (int i = 0; i < jsonArray.size(); i++){
				String toEmail = jsonArray.getString(i);
				//spring boot已进行邮件格式校验故不在此校验，直接添加
				nameAndPaths.add(toEmail);
			}
		} catch (Exception e) {
			log.error("接收邮箱格式错误");
			e.printStackTrace();
		}

		return nameAndPaths;
	}

	//获取附件名称和路径
	public static List<String[]> getNameAndPath(String nameAndPathJSON){
		List<String[]> nameAndPaths = new ArrayList<String[]>();
		
		JSONArray jsonArray = null;
		try {
			jsonArray = JSON.parseArray(nameAndPathJSON);
			for (int i = 0; i < jsonArray.size(); i++){
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            String[] file = new String[2];
	            file[0] = jsonObject.getString("name");
	            file[1] = jsonObject.getString("path");
	            nameAndPaths.add(file);
			}
		} catch (Exception e) {
			log.error("邮件附件信息有误");
			e.printStackTrace();
		}
		return nameAndPaths;
	}
	
	//获取图片cid和路径
	public static List<String[]> getCidAndPath(String cidAndPathJSON){
		List<String[]> cidAndPaths = new ArrayList<String[]>();
		
		JSONArray jsonArray = null;
		try {
			jsonArray = JSON.parseArray(cidAndPathJSON);
			for (int i = 0; i < jsonArray.size(); i++){
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	            String[] file = new String[2];
	            file[0] = jsonObject.getString("cid");
	            file[1] = jsonObject.getString("path");
	            cidAndPaths.add(file);
			}
		} catch (Exception e) {
			log.error("邮件图片信息有误");
			e.printStackTrace();
		}
		return cidAndPaths;
	}


}
