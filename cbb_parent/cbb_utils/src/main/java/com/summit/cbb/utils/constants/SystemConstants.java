package com.summit.cbb.utils.constants;

import java.text.DecimalFormat;

/**
 * @author xjtuhgd
 * @date 2019/07/09
 */
public class SystemConstants {

    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 15;

    public static final DecimalFormat DF0 = new DecimalFormat("#0");
    public static final DecimalFormat DF1 = new DecimalFormat("#0.0");
    public static final DecimalFormat DF2 = new DecimalFormat("#0.00");
    public static final DecimalFormat DF3 = new DecimalFormat("#0.000");
    public static final DecimalFormat DF7 = new DecimalFormat("#0.0000000");

    public static final int[] ADCD_LEV_INDEX = {2, 4, 6, 9, 12};
    public static final int ADCD_LENGTH = 6;

    /**
     * 英文句号
     */
    public static final String PERIOD = "\\.";
    /**
     * 中划线
     */
    public static final String MID_LINE = "-";
    /**
     * 空格
     */
    public static final String SPACE = " ";
    /**
     * 下划线
     */
    public static final String UNDER_LINE = "_";
    /**
     * 英文逗号
     */
    public static final String COMMA = ",";
    public static final String STR_ZERO = "0";
    public static final String STR_ONE = "1";
    public static final String STR_TWO = "2";

    /**
     * 年
     */
    public static final String YEAR = "yyyy";
    /**
     * 年月
     */
    public static final String YEAR_MONTH = "yyyy-MM";
    /**
     * 年月日
     */
    public static final String DATE = "yyyy-MM-dd";
    /**
     * 中文年月
     */
    public static final String ZH_YEAR_DATE = "yyyy年MM月";
    /**
     * 年月日时分秒
     */
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * 月日时分秒
     */
    public static final String MONTH_DATE_TIME = "MM-dd HH:mm:ss";

    public static final String EXCEL_SUFFIX = ".xlsx";
    public static final String WORD_SUFFIX = ".docx";
    public static final String PNG_SUFFIX = ".png";
    public static final String DIR_IMG = "img";
    public static final String HTML_SUFFIX = ".html";

    public static final String DIR_EXCEL = "excel";

    public static final String EIGHT_AM = "08:00:00";
    public static final String TWELVE_AM = "12:00:00";
    public static final String DAY_BEGIN = " 00:00:00";
    public static final String MONTH_BEGIN = "-01 00:00:00";
    public static final String YEAR_BEGIN = "-01-01 00:00:00";
	
	 /**
     * charts 间距
     */
	public static final double GAP_01 = 0.1;
    public static final double GAP_02 = 0.5;

    /**
     * 日期最大值
     */
    public static final int MAX_DATE = 31;

    /**
     * 测站编码长度
     */
    public static final int STCD_LENGTH = 8;

    /**
     * 最小二乘法拟合得到一元几次密函数
     */
    public static final int ORDINARY_LEAST_SQUARES_LEVEL = 2;

    /**
     * 中文数字
     */
    public static final String[] NUM_ZH = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    /**
     * 罗马数字
     */
    public static final String[] NUM_LM = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    /**
     * 数据字典中测站筛选条件根节点标识
     */
    public static final String STCD_FILTER = "STCD_FILTER";
    /**
     * 数据字典中测站筛选条件水文地质单元标识
     */
    public static final String CHFCID = "CHFCID";

    public static final String BD_THD = "BD_THD";
    public static final String ENABLE = "1";
    /**
     * 埋深变幅超阈值
     */
    public static final String BDBF_ABOVE_THD = "BDBF_ABOVE_THD";
    /**
     * 埋深大于历史最大值
     */
    public static final String BD_ABOVE_MXBD = "BD_ABOVE_MXBD";
    /**
     * 埋深大于井深
     */
    public static final String BD_ABOVE_OWDP = "BD_ABOVE_OWDP";
    /**
     * 埋深小于0且非自流井
     */
    public static final String BD_BELOW_ZERO_NOT_AW = "BD_BELOW_ZERO_NOT_AW";

    /**
     * 数据字典中监测方式标识
     */
    public static final String MNM = "MNM";
    /**
     * 数据字典中监测频次标识
     */
    public static final String MNF = "MNF";

    /**
     * 数据字典中开采量的计量方式标识
     */
    public static final String MMNMY = "MMNMY";
    

    /**
     * 数据字典泉流量的计量方式标识
     */
    public static final String SQ_MMT = "SQ_MMT";

    /**
     * 方法类型
     */
    public static final String METHOD_SAVE = "save";

    /**
     * 监测方式：人工
     */
    public static final String MNM_0 = "0";

    /**
     * 数据字典中雨量的阈值 --整编分析中用到
     */
    public static final String PPTNTHD = "PPTN_THD";

    /**
     * 全年
     */
    public static final String WHOLE_YEAR = "00";

    /**
     * 前数替补天数
     */
    public static final Integer ZB_USE_AVG_BEFORE_NUM = 10;
    /**
     * 前数替补天数字典表code
     */
    public static final String ZB_USE_AVG_BEFORE = "ZB_USE_AVG_BEFORE_NUM";

}
