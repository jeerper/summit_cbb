package com.summit.util;


import java.math.BigDecimal;

/**
 * 
 * @author yt
 *
 */
public class NumberUtil {

	public static long random(long max) {
		return (long) (Math.random() * max);
	}

	public static int random(int max) {
		return (int) (Math.random() * max);
	}


	public static BigDecimal round(BigDecimal money, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal one = new BigDecimal("1");
		return money.divide(one, scale, BigDecimal.ROUND_HALF_UP);
	}


	public static String decimal(String number, int digits, boolean fullZero) {
		int index = number.indexOf(".");
		int endWith = 0;
		String value = number;
		if (index > 0) {
			if (digits == 0) {
				endWith = index;
			} else {
				endWith = index + 1 + digits;
			}
			if (endWith != value.length()) {
				value = StringUtil.subString(number, 0, endWith);
			}
			if (fullZero) {
				value = StringUtil.fullStringAfter(value, '0', endWith);
			}
		} else if (fullZero) {
			if (digits > 0) {
				value = StringUtil.appendStringNotNull(".", value, StringUtil.getNString('0', digits));
			}
		}
		return value;
	}

	public static void main(String[] args) {
		System.out.println(decimal("5", 0, true));
		System.out.println(decimal("5.1", 0, true));
		System.out.println(decimal("5.0", 0, true));
		System.out.println(decimal("5.01111", 0, true));
		System.out.println(decimal("5.22112", 0, false));
		System.out.println(decimal("5", 0, false));
		System.out.println(decimal("5.1", 0, false));
	}
}
