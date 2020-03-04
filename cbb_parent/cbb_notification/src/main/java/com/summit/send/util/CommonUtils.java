package com.summit.send.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName CommonUtils
 * @Description TODO
 * @Author maoyuxuan
 * @Date 2020/2/21 10:56
 * @Version 1.0
 **/
public class CommonUtils {

    /**
     * 生成32位UUID
     * @return
     */
    public static String get32UUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 从request中获取请求方IP
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String filePathListToString(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                stringBuffer.append(str);
                stringBuffer.append(";");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static List<String> filePathStringToList(String filePathString) {
        if (!StringUtils.isEmpty(filePathString)) {
            String[] fileArray = filePathString.split(";");
            return Arrays.asList(fileArray);
        } else {
            return null;
        }
    }
}
