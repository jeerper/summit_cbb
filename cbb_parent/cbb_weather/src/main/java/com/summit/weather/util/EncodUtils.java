package com.summit.weather.util;

import java.io.UnsupportedEncodingException;

public class EncodUtils {

    public static boolean isUTF8(String key) {
        try {
            key.getBytes("UTF-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

}
