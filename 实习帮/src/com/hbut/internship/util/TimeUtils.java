package com.hbut.internship.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 * 
 * @author Nie
 * 
 */
public class TimeUtils {
	/**
	 * 比较两个时间的前后顺序
	 * 
	 * @param DATE1
	 *            时间1
	 * @param DATE2
	 *            时间2
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 时间转化
	 * 
	 * @param minute
	 *            分钟
	 * @return
	 */
	public static String timeFormatter(int minute) {
		if (minute < 60) {
			return minute + "分钟";
		} else if (minute % 60 == 0) {
			return minute / 60 + "小时";
		} else {
			int hour = minute / 60;
			int minute1 = minute % 60;
			return hour + "小时" + minute1 + "分钟";
		}

	}
}
