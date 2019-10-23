package com.summit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yt
 */
public class LoadCommonConfig {

    private static Properties prop = null;

    /**
     * 资源文件读取位置
     */
    private static String propFileName = "common.properties";

    /**
     * 日志文件管理
     */
    private static final Logger log = LoggerFactory.getLogger(LoadCommonConfig.class);

    private synchronized static void loadProperties() {
        if (null == prop) {
            prop = new Properties();
            try {
                prop.load(LoadCommonConfig.class.getClassLoader().getResourceAsStream(propFileName));
            } catch (IOException e) {
                log.error("读取资源配置文件错误," + e.toString());
            }
        }
    }

    public static String getProperty(String propName) {
        String strRet = "";
        loadProperties();
        try {
            strRet = prop.getProperty(propName).trim();
        } catch (Exception e) {
            log.error("读取资源配置文件中的字段错误,字段:" + propName + "错误信息:" + e.toString());
        }
        return strRet;
    }

    public static void setProperty(String propKey, String value) {
        loadProperties();
        try {
            prop.setProperty(propKey, value);
        } catch (Exception e) {
            log.error("设置字段的值错误,错误信息:" + e.toString());
        }
        return;
    }

    public static void saveProperty() {
        try {
            File file = new File(propFileName);
            FileOutputStream fos = new FileOutputStream(file);
            prop.store(fos, null);
            fos.close();
        } catch (Exception e) {
            log.error("更新字段的值错误,错误信息:" + e.toString());
        }
        return;
    }

    public String getPropFileName() {
        return propFileName;
    }

    public void setPropFileName(String propFileName) {
        LoadCommonConfig.propFileName = propFileName;
    }

}
