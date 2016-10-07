package com.hbut.internship.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据身份证号计算年龄
 */
public class CalculateAgeUtil {

	public static int IdNOToAge(String IdNO) {
		int leh = IdNO.length();
		String dates = "";
		if (leh == 18) {
			int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
			dates = IdNO.substring(6, 10);
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(new Date());
			int u = Integer.parseInt(year) - Integer.parseInt(dates);
			return u;
		} else {
			dates = IdNO.substring(6, 8);
			return Integer.parseInt(dates);
		}

	}
}
