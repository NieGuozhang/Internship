package com.hbut.internship.util;

import java.util.ArrayList;
import java.util.List;

import com.internship.model.FirstClassItem;
import com.internship.model.SecondClassItem;

/**
 * 筛选工具类
 * 
 * @author Nie
 * 
 */
public class FiterUtil {

	public static void initData(List<FirstClassItem> firstList) {
		// 1薪水待遇
		ArrayList<SecondClassItem> salaryList = new ArrayList<SecondClassItem>();
		salaryList.add(new SecondClassItem(101, "3K以下"));
		salaryList.add(new SecondClassItem(102, "3K-6K"));
		salaryList.add(new SecondClassItem(103, "6K以上"));
		firstList.add(new FirstClassItem(1, "薪资", salaryList));
		// 2实习地点
		ArrayList<SecondClassItem> poaddressList = new ArrayList<SecondClassItem>();
		poaddressList.add(new SecondClassItem(201, "天津"));
		poaddressList.add(new SecondClassItem(202, "北京"));
		poaddressList.add(new SecondClassItem(203, "秦皇岛"));
		poaddressList.add(new SecondClassItem(204, "沈阳"));
		poaddressList.add(new SecondClassItem(205, "大连"));
		poaddressList.add(new SecondClassItem(206, "哈尔滨"));
		poaddressList.add(new SecondClassItem(207, "锦州"));
		poaddressList.add(new SecondClassItem(208, "上海"));
		poaddressList.add(new SecondClassItem(209, "杭州"));
		poaddressList.add(new SecondClassItem(210, "南京"));
		poaddressList.add(new SecondClassItem(211, "嘉兴"));
		poaddressList.add(new SecondClassItem(212, "苏州"));
		firstList.add(new FirstClassItem(2, "实习地点", poaddressList));
		// 3学历要求
		ArrayList<SecondClassItem> educationList = new ArrayList<SecondClassItem>();
		educationList.add(new SecondClassItem(301, "无要求"));
		educationList.add(new SecondClassItem(302, "专科"));
		educationList.add(new SecondClassItem(303, "本科"));
		educationList.add(new SecondClassItem(304, "研究生"));
		firstList.add(new FirstClassItem(3, "学历要求", educationList));

		firstList.addAll(firstList);
	}
}
