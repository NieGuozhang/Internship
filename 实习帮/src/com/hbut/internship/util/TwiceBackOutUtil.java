package com.hbut.internship.util;

/**
 * 连续点击两次返回键 提示退出应用
 * @author Nie
 *
 */
public class TwiceBackOutUtil {
	public static long firstTimeBack = (long) 0;

	/**
	 * @param outTipStr
	 *            退出提示
	 * @param twiceBackIntervalTime
	 *            能够退出的两次按返回键的时间间隔
	 * @return
	 */
	public static boolean twiceBackOut(String outTipStr,
			int twiceBackIntervalTime) {
		long secondTimeBack = System.currentTimeMillis();
		if (secondTimeBack - firstTimeBack > twiceBackIntervalTime) {// 如果两次按键时间间隔大于twiceBackIntervalTime毫秒，则不退出
			ToastUtil.showtoast(outTipStr);
			firstTimeBack = secondTimeBack;// 更新firstTimeBack
			return true;
		} else {
			System.exit(0);// 否则退出程序，可以修改退出方式
			return false;
		}
	}
}
