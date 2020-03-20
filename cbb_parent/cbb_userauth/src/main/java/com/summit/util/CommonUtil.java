package com.summit.util;

import com.summit.common.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtil {

    public static ThreadLocal<DateFormat> timeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(CommonConstants.timeFormat));
    public static ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(CommonConstants.dateFormat));
    public static ThreadLocal<DateFormat> snapshotTimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(CommonConstants.snapshotTimeFormat));
    public static ThreadLocal<DateFormat> frontTimeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(CommonConstants.frontTimeFormat));


    /**
     * string类型时间转换为Date
     * @param time 待转换string类型时间
     * @param pattern 格式
     * @return Date类型日期
     * @throws ParseException 日期转换异常
     */
    public static Date parseStr2Date(String time,String pattern) throws ParseException {
        if (CommonUtil.isEmptyStr(time) || CommonUtil.isEmptyStr(pattern))
            return null;
        return new SimpleDateFormat(pattern).parse(time);
    }

    /**
     * Date类型时间格式化为string
     * @param time 待转换string类型时间
     * @param pattern 格式
     * @return String类型日期
     */
    public static String formmatDate2Str(Date time,String pattern) {
        if (time == null || CommonUtil.isEmptyStr(pattern))
            return null;
        return new SimpleDateFormat(pattern).format(time);
    }

    /**
     * List集合去重
     * @param list 待去重List集合
     */
    public static void removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
    }

    /**
     * List集合判空
     * @param list 待判空List集合
     * @return 是否为空
     */
    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 数组判空
     * @param arr 待判空数组
     * @return 数组是否为空
     */
    public static boolean isEmptyArr(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * 字符串判空
     * @param str 待判空字符串
     * @return 字符串是否为空
     */
    public static boolean isEmptyStr(String str) {
        return str == null || str.trim().length() == 0;
    }




    /**
     *
     * @param beginTime 开始时间
     * @param endTime 截至时间
     * @return true 或 false
     * @throws ParseException
     */
    public static boolean isInTime(Date beginTime,Date endTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Date date=new Date();
        String nowtime = df.format(date);
        long nowtime1 = df.parse(nowtime).getTime();
        long biegtime = beginTime.getTime();
        long endtimeTime = endTime.getTime();
        if (nowtime1>=biegtime && nowtime1<=endtimeTime){
            return true;
        }
        return false;
    }

    /**
     *  读取文件的流
     * @param path
     * @return 文件内容
     */
    public static String readFile(String path){
        BufferedReader  breader=null;
        StringBuffer sbf = new StringBuffer();
        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader =null;
        try {
            fileInputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            breader=new BufferedReader(inputStreamReader);
            String tempString;
            while ((tempString = breader.readLine()) != null){
                sbf.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (breader !=null){
                    breader.close();
                }
                if (inputStreamReader !=null){
                    inputStreamReader.close();
                }
                if (fileInputStream !=null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbf.toString();
    }

    /**
     *  把图片转换成base64
     * @param path  图片路径
     * @return  图片的base64
     * @throws IOException
     */
    public  static String imageToBase64Str(String path) {
        String baseStr =null;
        FileInputStream fis=null;
        try {
             fis = new FileInputStream(new File(path));
            byte[] read = new byte[1024];
            int len = 0;
            List<byte[]> blist=new ArrayList<byte[]>();
            int ttllen=0;
            while((len = fis.read(read))!= -1){
                byte[] dst=new byte[len];
                System.arraycopy(read, 0, dst, 0, len);
                ttllen+=len;
                blist.add(dst);
            }
            fis.close();
            byte[] dstByte=new byte[ttllen];
            int pos=0;
            for (int i=0;i<blist.size();i++){
                if (i==0){
                    pos=0;
                }
                else{
                    pos+=blist.get(i-1).length;
                }
                System.arraycopy(blist.get(i), 0, dstByte, pos, blist.get(i).length);
            }
            baseStr= Base64.getEncoder().encodeToString(dstByte);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis !=null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  baseStr;
    }
    /**
     * TODO 除法运算，保留小数
     * @param denominator 被除数 分母
     * @param numerator 除数 分子
     * @return 商
     */
    public static Double toFloat(int denominator,int numerator) {
        // TODO 自动生成的方法存根
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        return Double.valueOf(df.format((float)denominator/numerator));
    }

}
