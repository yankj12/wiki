package com.yan.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

//	public static void main(String[] args) throws Exception {
//		Date date = formatDateStr("2017/12/18 10:00");
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		System.out.println(simpleDateFormat.format(date));
//	}
	
	public static Date formatDateStr(String dateStr) throws Exception {
		Date date = null;
		
		// yyyy/MM/dd HH:mm, yyyy/MM/dd HH:mm:ss, yyyy-MM-dd HH:mm:ss 或者 yyyy-MM-dd HH:mm:ss 这些格式都可以通过正则表达式获取到年月日时分秒
		// 使用正则表达式获取年月日时分秒
		
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		
		String regEx = "(\\d+)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(dateStr);
		
		// find()方法是部分匹配，是查找输入串中与模式匹配的子串，如果该匹配的串有组还可以使用group()函数。
		// matches()是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()。
		int index = 0;
		while (matcher.find()) {
			String str = matcher.group();
			//System.out.println(str);
			
			if(index == 0) {
				year = Integer.parseInt(str);
			}else if(index == 1) {
				month = Integer.parseInt(str);
			}else if(index == 2) {
				day = Integer.parseInt(str);
			}else if(index == 3) {
				hour = Integer.parseInt(str);
			}else if(index == 4) {
				minute = Integer.parseInt(str);
			}else if(index == 5) {
				second = Integer.parseInt(str);
			}
			
			index++;
		}
		
		StringBuilder newDateStr = new StringBuilder();
		newDateStr.append(year).append("/").append(month > 10 ? month : "0" + month).append("/").append(day > 10 ? day : "0" + day).append(" ")
				.append(hour > 10 ? hour : "0" + hour)
				.append(":").append(minute > 10 ? minute : "0" + minute)
				.append(":").append(second > 10 ? second : "0" + second);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = simpleDateFormat.parse(newDateStr.toString());
		
		return date;
	}
}
