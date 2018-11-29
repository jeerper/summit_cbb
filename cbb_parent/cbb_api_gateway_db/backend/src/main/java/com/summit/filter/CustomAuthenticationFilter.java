package com.summit.filter;

import net.sf.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
/**
 * 
 * @author yt
 *
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws org.springframework.security.core.AuthenticationException {

		
		UsernamePasswordAuthenticationToken authRequest=null;
		
		JSONObject userJson = new JSONObject();
		if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
				|| request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
			//use jackson to deserialize json
			try (InputStream is = request.getInputStream()) {
				userJson =readJson(request);
                authRequest = new UsernamePasswordAuthenticationToken(
                		userJson.getString("username"), userJson.getString("password"));
			} catch (IOException e) {
				e.printStackTrace();
				authRequest = new UsernamePasswordAuthenticationToken("", "");
			} finally {
				setDetails(request, authRequest);
			}
		}else{
			userJson.put("username", request.getParameter("username"));
			userJson.put("password", request.getParameter("password"));
			authRequest = new UsernamePasswordAuthenticationToken(
            		userJson.getString("username"), userJson.getString("password"));
			setDetails(request, authRequest);

		}
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	public static JSONObject readJson(HttpServletRequest request) throws IOException
	{
		BufferedReader br;
		StringBuilder sb = null;
		String reqBody = null;
		br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line = null;
		sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		reqBody = URLDecoder.decode(sb.toString(), "UTF-8");
		JSONObject object=JSONObject.fromObject(reqBody);
		return object;
	}

}
