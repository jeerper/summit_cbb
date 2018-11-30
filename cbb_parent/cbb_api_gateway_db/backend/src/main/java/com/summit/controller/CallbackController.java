package com.summit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 
 * @author yt
 *
 */
@RestController
@ApiIgnore
public class CallbackController {


    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/code/redirect")
    public String getToken(@RequestParam String code){
    	System.out.println("code : " + code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        
        
        params.add("grant_type","authorization_code");
        params.add("code",code);
        params.add("client_id","yt1");
        params.add("client_secret","888888");
        params.add("redirect_uri","http://localhost:8769/code/redirect");
        System.out.println(params.toString());
        System.out.println("------------------------------------------");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8769/oauth/token", requestEntity, String.class);
        String token = response.getBody();
        System.out.println("token :" + token);
        JSONObject tokenJson = JSONObject.fromObject(token);
 	   HttpHeaders requestHeaders = new HttpHeaders();
 	  requestHeaders.set("Authorization", "Bearer " + tokenJson.getString("access_token"));
 	  
 	  //将token 加入到header中
 	  requestHeaders.add("token", "Bearer " + tokenJson.getString("access_token"));
 	  
 	  
 	  //请求组件demo
        //HttpEntity<MultiValueMap<String, String>> requestEntity1 = new HttpEntity<>(null, requestHeaders);

      //  ResponseEntity<String> response1 = restTemplate.postForEntity("http://localhost:8769/api/demo/testDemo1", requestEntity1, String.class);
       // String token1 = response1.getBody();
        //System.out.println(token1);
        return token;
    }

}
