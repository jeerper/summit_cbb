package com.summit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;

/**
 * restTemple调用rest接口工具类
 * 
 * @author 叶腾
 * @version 1.0
 */
public class RestZipUtil {

	public static String restPost(String url, JSONObject js) {

		String datahead = "";
		try {
			datahead = GzipUtils.compress(js.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		// Accept只能接受字符（text/plain ..）串并且是ISO-8859-1编码的其他的试过不行
		headers.add("Accept", "text/html;charset=ISO-8859-1");
		// 声明接受的格式为gzip
		headers.add("Accept-Encoding", "gzip"); 
		HttpEntity<String> formEntity = new HttpEntity<String>(datahead, headers);
		RestTemplate restTemplate = new RestTemplate();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, formEntity, String.class);
			ByteArrayInputStream in = new ByteArrayInputStream(response.getBody().getBytes("ISO-8859-1"));
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return out.toString();
	}

}
