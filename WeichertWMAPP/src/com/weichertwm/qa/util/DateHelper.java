package com.weichertwm.qa.util;

import java.text.*;
import java.util.*;

import com.weichertwm.qa.framework.Config;

public class DateHelper {
	/**
	 * <b>Description</b> Gets Current date
	 * @param          format format of the date
	 * @return         date Current date with specified format
	 */
	public static String getCurrentDate(String format){		
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
	private static Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    return cal.getTime();
	}
	/**
	 * <b>Description</b> Gets Yesterday date
	 * @param          format format of the date
	 * @return         date Yesterday date with specified format
	 */
	public static String getYesterdayDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(yesterday());
	}
	/**
	 * <b>Description</b> Gets Current date and time
	 * @param          format format of the date
	 * @return         date Current date with specified format
	 */
	public static String getCurrentDatenTime(String format){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}
	/**
	 * <b>Description</b> Gets Formatted time
	 * @param          time  long time
	 * @return         time  long time
	 */

	public static String getFormattedTime(long time){
		long timeMillis = time;
		long time1 = timeMillis / 1000;
		String seconds = Integer.toString((int)(time1 % 60));
		String minutes = Integer.toString((int)((time1 % 3600) / 60));
		String hours = Integer.toString((int)(time1 / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
		return hours+": "+minutes+": "+seconds;
	}

	/**
	 * <b>Description</b> Date Converter for excel values to E MMM dd HH:mm:ss Z yyyy format
	 * @param          excelDate  Date value to convert
	 * @return         formatedDate  formatted date
	 */
	public static String dateConverter(String excelDate){	
		String formatedDate = null;
		try{
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date = (Date)formatter.parse(excelDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			formatedDate = (cal.get(Calendar.MONTH) + 1) + "/"+cal.get(Calendar.DATE) + "/" +         cal.get(Calendar.YEAR);
		}catch(Exception e){
			e.printStackTrace();	
		}
		return formatedDate;
	}

	/**
	 * <b>Description</b> Date Converter for excel values with the specified Date Format
	 * @param          excelDate  Date value to convert
	 * @param          dateFormat format to convert
	 * @return         formatedDate  formatted date
	 */
	@SuppressWarnings("deprecation")
	public static String dateConverter(String excelDate,String dateFormat){
		Date myDate = new Date(excelDate);
		String date ="";
		try{
			DateFormat formatter = new SimpleDateFormat(dateFormat);
			date = formatter.format(myDate);
			date = date.replace("-", "/");		
		}catch(Exception e){
			return "";
		}

		return date;
	}	

	

	/**
	 * <b>Description</b> Adds no of dates to System Date
	 * @param          noOfDay  No. of days
	 * @param          noOfMonths  No. of Months
	 * @param          noOfYear  No. of Years
	 */
	public static String addDaysToSysDate(int noOfDay, int noOfMonths, int noOfYear) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(Config.getEnvDetails("dateFormat"));
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, noOfDay);		
			calendar.add(Calendar.MONTH, noOfMonths);
			calendar.add(Calendar.YEAR, noOfYear);
			return sdf.format(calendar.getTime());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}


	/**
	 * <b>Description</b> Gets Latest current time in milli seconds
	 */
	public static long getLastsetTimeinmili(){
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}
	
	private static Date tomorrow() {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, 1);
	    return cal.getTime();
	}
	
	/**
	 * <b>Description</b> Gets Tomorrow date
	 * @param          format format of the date
	 * @return         date Tomorrow date with specified format
	 */
	public static String getTomorrowDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(tomorrow());
	}
	
	/**
	 * Method used to get the required Date with the number of days differences
	 * @param dateFormat
	 * @param numberOfDaysDeviation
	 * @return
	 */
	public static String getRequiredDate(String dateFormat, String numberOfDaysDeviation) {
		Date currentDay = new Date();
		System.out.println("Today's date: "+currentDay.toString());
		Calendar calender = null;
		int days = 0;
		days = Integer.parseInt(numberOfDaysDeviation);
		SimpleDateFormat dformat = new SimpleDateFormat(dateFormat);
		dformat.setTimeZone(TimeZone.getTimeZone("UTC-08:00"));
		calender = Calendar.getInstance();
		calender.setTime(currentDay);
		calender.add(Calendar.DATE,days);
		return dformat.format(calender.getTime());
	}
	
	public static String getTimeDifference(String strTime2, String strTime1) {
		int totalHours = Integer.parseInt(strTime2.split(":")[0].trim()) - Integer.parseInt(strTime1.split(":")[0].trim());
		int totalMinutes = Integer.parseInt(strTime2.split(":")[1].trim()) - Integer.parseInt(strTime1.split(":")[1].trim());
		if (totalMinutes >= 60) {
			totalHours++;
			totalMinutes = totalMinutes % 60;
		}
		String diffTime = totalHours+":"+totalMinutes;
		System.out.println(diffTime);
		return diffTime;
	}

}
