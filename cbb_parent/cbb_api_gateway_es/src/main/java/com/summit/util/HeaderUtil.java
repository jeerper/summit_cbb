package com.summit.util;

import org.springframework.http.HttpEntity;

/**
 * @author yt
 */
public class HeaderUtil {

    public synchronized static boolean isZip(HttpEntity<String> formEntity) {
        String gzip = "[gzip]";
        String acceptEncoding = "accept-encoding";
        if (formEntity.getHeaders().get(acceptEncoding).toString().equalsIgnoreCase(gzip)) {
            return true;
        } else {
            return false;
        }
    }
}
