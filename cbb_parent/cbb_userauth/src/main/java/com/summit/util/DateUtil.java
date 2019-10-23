package com.summit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期操作类
 *
 * @author 叶腾
 */
public class DateUtil {

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, DateFormat> sdfMap = new Hashtable<>();
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HMS1 = "yyyyMMddHHmmss";
    public static final String YMD_HMS_SSS = "yyyy-MM-dd HH:mm:ss.sss";

    static {
        sdfMap.put(YMD_HM, new SimpleDateFormat(YMD_HM));
        sdfMap.put(YMD_HMS, new SimpleDateFormat(YMD_HMS));
        sdfMap.put(YMD, new SimpleDateFormat(YMD));
        sdfMap.put(YMD_HMS_SSS, new SimpleDateFormat(YMD_HMS_SSS));
        /*
         * sdfMap.put("yyyy/MM/dd HH:mm", new
         * SimpleDateFormat("yyyy/MM/dd HH:mm"));
         * sdfMap.put("yyyy/MM/dd HH:mm:ss", new
         * SimpleDateFormat("yyyy/MM/dd HH:mm:ss")); sdfMap.put("yyyy/MM/dd",
         * new SimpleDateFormat("yyyy/MM/dd"));
         */
    }

    /**
     * 获取DateFormat对象
     *
     * @param format date格式
     * @return DateFormat对象
     */
    public static DateFormat getDateFormat(String format) {
        if (sdfMap.get(format) == null) {
            DateFormat sf = new SimpleDateFormat(format);
            sdfMap.put(format, sf);
        }
        return sdfMap.get(format);
    }

    /**
     * 用给定的样式格式化给定的日期（字符串）
     *
     * @param format  日期样式
     * @param dateStr 目标日期字符串
     * @return Date对象
     */
    public static Date parse(String dateStr, String... format) {
        Date date = null;
        for (int i = 0; i < format.length; i++) {
            try {
                date = getDateFormat(format[i]).parse(dateStr);
                if (date != null) {
                    return date;
                }
            } catch (Exception e) {
            }
        }
        return date;
    }

    /**
     * 将给定的日期格式化为默认的样式:yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr 给定的日期
     * @return date对象
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, YMD_HM, YMD_HMS, YMD);
    }

    /**
     * 扩展parse,支持季度qq,半年hy
     *
     * @param dateStr dateStr
     * @param format  format
     * @return date对象
     */
    public static Date parseExtend(String dateStr, String format) {
        int mm = 0;
        String qq = "qq";
        String hy = "hy";

        if (format.indexOf(qq) >= 0) {
            format = format.replace("qq", "MM");
            mm = 3;
        } else if (format.indexOf(hy) >= 0) {
            format = format.replace("hy", "MM");
            mm = 6;
        }
        Date date = parse(dateStr, format);
        if (mm > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month * mm);
            date = calendar.getTime();
        }
        return date;
    }


    /**
     * 某個月份的天數可能拥有的最大值
     *
     * @param year  year
     * @param month month
     * @return int最大值
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        int maxDate = 0;
        if (year != 0 && month != 0) {
            a.clear();
            a.set(1, year);
            a.set(2, month - 1);
            maxDate = a.getActualMaximum(5);
        }
        return maxDate;
    }

    /**
     * 查询两时间间隔
     *
     * @param startTime startTime 传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
     * @param endTime   endtime
     * @return string
     */
    public static String getInterval(String startTime, String endTime) {
        String interval = null;
        SimpleDateFormat sd = new SimpleDateFormat(YMD_HMS);
        ParsePosition pos1 = new ParsePosition(0);
        ParsePosition pos2 = new ParsePosition(0);
        Date d1 = (Date) sd.parse(startTime, pos1);
        Date d2 = (Date) sd.parse(endTime, pos2);
        // 得出的时间间隔是毫秒
        long time = d2.getTime() - d1.getTime();
        //毫秒转换为秒
        int msSize = 1000;
        //判断刚刚时间
        int justNowSize = 20;
        // 天转换为毫秒
        int daySize = 3600000;
        //小时转换为毫秒
        int hourSize = 60000;
        //一天小时size
        int day = 24;
        //一小时分钟size
        int hour = 60;
        if (time / msSize < justNowSize && time / msSize >= 0) {
            // 如果时间间隔小于20秒则显示“刚刚”
            interval = "刚刚";

        } else if (time / daySize < day && time / daySize > 0) {
            // 如果时间间隔小于24小时则显示多少小时前
            // 得出的时间间隔的单位是小时
            int h = (int) (time / daySize);
            interval = h + "小时前";

        } else if (time / hourSize < hour && time / hourSize > 0) {
            // 如果时间间隔小于60分钟则显示多少分钟前
            // 得出的时间间隔的单位是分钟
            int m = (int) ((time % daySize) / hourSize);
            interval = m + "分钟前";

        } else if (time / msSize < hour && time / msSize > 0) {
            // 如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % hourSize) / msSize);
            interval = se + "秒前";

        } else {
            // 大于24小时，则显示天
            // 得出的时间间隔的单位是天
            int d = (int) (time / (daySize * 24));
            interval = d + "天前";
        }
        return interval;
    }

    /**
     * 计算两个时间差，要精确到小时
     * date1 开始时间
     * date2 结束时间
     **/
    public static double jisuan(String date1, String date2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date start = sdf.parse(date1);
        Date end = sdf.parse(date2);
        long cha = end.getTime() - start.getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        //logUtil.info("开始时间:"+date1+"结束时间:"+date2+"相差:"+result+"小时");
        return result;
    }

    /**
     * 获取给定时间月的最大天数
     *
     * @param date
     * @return int
     */
    public static int getMaxDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 获取当前时间到23.59分59秒剩余毫秒数
     *
     * @return
     */
    public static long getTimeToNight() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long nightTime = calendar.getTimeInMillis();
        return nightTime - currentTime;
    }

    /**
     * 获取当前时间到指定时间剩余毫秒数
     *
     * @return
     */
    public static long getSecondToNight() {
        return (long) Math.ceil(getTimeToNight() / 1000);
    }

    /**
     * 获取当前时间到指定时间剩余毫秒数
     *
     * @return
     */
    public static long getTimeToDate(Date date) {
        long nightTime = date.getTime();
        return nightTime - System.currentTimeMillis();
    }

    /**
     * 获取当前时间到指定时间剩余秒数
     *
     * @return
     */
    public static long getSecondToDate(Date date) {
        return (long) Math.ceil(getTimeToDate(date) / 1000);
    }

    /**
     * 获取当前时间日期最后时间
     *
     * @return
     */
    public static Date dateToNight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取当前日期0点时间
     *
     * @return
     */
    public static Date dateToMorining(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取给定时间所处星期的第一时间
     *
     * @param date
     * @return
     */
    public static Calendar getWeekFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取第一时间点 当前年,不可变更
     *
     * @param field Calendar.field
     * @return
     */
    public static Calendar getFirstDate(int field, Date date) {
        if (field == Calendar.DAY_OF_WEEK) {
            return getWeekFirstDate(date);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (field) {
            case Calendar.YEAR:
                calendar.set(Calendar.MONTH, 0);
                break;
            case Calendar.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.DAY_OF_MONTH:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case Calendar.HOUR_OF_DAY:
                calendar.set(Calendar.MINUTE, 0);
                break;
            case Calendar.MINUTE:
                calendar.set(Calendar.SECOND, 0);
                break;
            case Calendar.SECOND:
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            default:
                break;
        }
        return calendar;
    }

    /**
     * 将时间转为字符串
     *
     * @param dateTimeType
     * @param date
     * @return
     */
    public static String DTFormat(String aMask, Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat(aMask);
        return yearFormat.format(date);
    }

    /**
     * 获取第一时间点 当前年,不可变更
     *
     * @param field Calendar.field
     * @return
     */
    public static Calendar getLastDate(int field, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (field) {
            case Calendar.YEAR:
                calendar.set(Calendar.MONTH, 11);
                break;
            case Calendar.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
                break;
            case Calendar.DAY_OF_MONTH:
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                break;
            case Calendar.HOUR_OF_DAY:
                calendar.set(Calendar.MINUTE, 59);
                break;
            case Calendar.MINUTE:
                calendar.set(Calendar.SECOND, 59);
                break;
            case Calendar.SECOND:
                calendar.set(Calendar.MILLISECOND, 999);
                break;
            default:
                break;
        }
        return calendar;
    }

    /**
     * 获取次时间点,如次月,次日
     *
     * @param field Calendar.field
     * @return
     */
    public static Calendar addDate(Date date, int field, int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, add);
        return calendar;
    }


    static Pattern pattern1 = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");

    public static String returnEsDateFormat(String timeStr) {
        String returnFormat = "";
        String secondFormat = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        String ymdFormat = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
        String hourFormat = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3])";
        String minFormat = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]";
        if (vaildFormat(secondFormat, timeStr)) {
            returnFormat = timeStr + ".000";
            return returnFormat;
        }
        if (vaildFormat(ymdFormat, timeStr)) {
            returnFormat = timeStr + " 00:00:00.000";
            return returnFormat;
        }

        if (vaildFormat(hourFormat, timeStr)) {
            returnFormat = timeStr + "00:00.000";
            return returnFormat;
        }

        if (vaildFormat(minFormat, timeStr)) {
            returnFormat = timeStr + "00.000";
            return returnFormat;
        }
        return "格式错误：请传入正确的格式 eg:yyyy-mm-dd HH:mm:ss";
    }

    private static boolean vaildFormat(String format, String timeStr) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(timeStr);
        boolean vaild = false;
        if (matcher.matches()) {
            vaild = true;
        }
        return vaild;
    }

    /**
     * 验证时间字符串格式输入是否正确
     *
     * @param timeStr
     * @return
     */
    public static boolean valiDateTimeWithLongFormat(String timeStr) {
		/*String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
				+ "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";*/

        String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9].[0-9][0-9][0-9]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(timeStr);
        int febSize = 28;
        if (matcher.matches()) {
            pattern = pattern1;
            matcher = pattern.matcher(timeStr);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > febSize) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m - 1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    }
}
