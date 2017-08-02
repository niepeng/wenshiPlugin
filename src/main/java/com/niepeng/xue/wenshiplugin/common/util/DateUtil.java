package com.niepeng.xue.wenshiplugin.common.util;
/**
 * Created by lsb on 17/8/2.
 */


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/8/2
 */

public class DateUtil {

  public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

  public static final String DEFAULT_DATE_FMT_NODATE = "HH:mm:ss";

  public static final String DEFAULT_DATE_FMT_NO = "yyyy-MM-dd";

  public static final String DEFAULT_DATE_FMT_NOSS = "yyyy-MM-dd HH:mm";

  public static final String DATE_FMT_YMD = "yyyy年MM月dd日";

  public static final String DATE_FMT_MD = "MM月dd日";

  public static final String DATE_FMT_MD_HM = "MM月dd日 HH:mm";

  public static final String DATE_YMDHMS = "yyyyMMddHHmmss";
  public static final String DATE_YMDHMS_SMALL = "yyMMddHHmmss";

  public static final String DATE_YMD = "yyyyMMdd";

  public static final String DATE_YD = "yyyyMM";

  public static final String DATE_YMD_SIMPLE = "yyMMdd";

  public static final String DEFAULT_DATE_FMT_NODATE_NOSEC = "HH:mm";

  public static final String DATE_FMT_MD_AND_HM = "MM-dd HH:mm";

  public static void main(String[] args) {
    Date date =  new Date();
    System.out.println(format(date));
    System.out.println(format(getUTCTime(date)));
  }

  /**
   * 当前时间转换成 UTC时间
   */
  public static Date getUTCTime(Date date) {
    // 1、取得本地时间：
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    // 2、取得时间偏移量：
    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 3、取得夏令时差：
    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
    // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
    return cal.getTime();
  }

  public static Date parse(String input) {
    return parse(input, DEFAULT_DATE_FMT);
  }

  public static Date parseNoException(String input) {
    try {
      return parse(input, DEFAULT_DATE_FMT);
    } catch (Exception e) {
      return null;
    }
  }

  public static Date parseNoSSNoException(String input) {
    try {
      return parse(input, DEFAULT_DATE_FMT);
    } catch (Exception e) {
      try {
        return parse(input, DEFAULT_DATE_FMT_NOSS);
      } catch (Exception e1) {
        return null;
      }
    }
  }

  public static Date long2Date(long time) {
    return new Date(time);
  }

  public static String format(Date date, String fmt) {
    try {
      if(date == null){
        return "";
      }
      SimpleDateFormat sdf = new SimpleDateFormat(fmt);
      return sdf.format(date);
    } catch (Exception e) {
      return null;
    }
  }

  public static String format(Date date) {
    return format(date, DEFAULT_DATE_FMT);
  }

  public static String formatNoException(Date date) {
    try {
      return format(date);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatNoTimeNoException(Date date) {
    try {
      return format(date,DEFAULT_DATE_FMT_NO);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatNoDateNoException(Date date) {
    try {
      return format(date, DEFAULT_DATE_FMT_NODATE);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatNoDateAndNoSecNoException(Date date) {
    try {
      return format(date, DEFAULT_DATE_FMT_NODATE_NOSEC);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatNoYearAndNoSecNoException(Date date) {
    try {
      return format(date, DATE_FMT_MD_AND_HM);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatNoYearAndNoTime(Date date){
    try {
      return format(date, DATE_FMT_MD);
    } catch (Exception e) {
      return null;
    }
  }

  public static String formatToYMDNoException(Date date){
    try {
      return format(date, DATE_FMT_YMD);
    } catch (Exception e) {
      return null;
    }
  }

  public static Date parse(String input, String fmt) {
    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    try {
      return sdf.parse(input);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static Date parseNoException(String input, String fmt) {
    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    try {
      return sdf.parse(input);
    } catch (Exception e) {
      return null;
    }
  }


  public static Date formatCurrentDayMax(Date date) {
    if(date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    return c.getTime();
  }

  public static Date formatCurrentDayMin(Date date) {
    if(date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    return c.getTime();
  }

  public static Date formatCurrentDayNoHour(Date date) {
    if(date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    return c.getTime();
  }

  public static int dayOfWeek(Date date) {
    if(date == null) {
      return 0;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.DAY_OF_WEEK);
  }

  public static Date currentMonthMin(Date date) {
    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.DAY_OF_MONTH, 1);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    return c.getTime();
  }

  public static int distanceDay(Date d1, Date d2) {
    if (d1 == null || d2 == null) {
      return 0;
    }

    Date date1 = parse(format(d1, DEFAULT_DATE_FMT_NO), DEFAULT_DATE_FMT_NO);
    Date date2 = parse(format(d2, DEFAULT_DATE_FMT_NO), DEFAULT_DATE_FMT_NO);

    return (int) Math.abs((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
  }

  public static int distanceDaySign(Date d1, Date d2) {
    if (d1 == null || d2 == null) {
      return 0;
    }

    Date date1 = parse(format(d1, DEFAULT_DATE_FMT_NO), DEFAULT_DATE_FMT_NO);
    Date date2 = parse(format(d2, DEFAULT_DATE_FMT_NO), DEFAULT_DATE_FMT_NO);

    return (int) (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
  }

  public static int distanceSeconds(Date d1, Date d2) {
    if (d1 == null || d2 == null) {
      return 0;
    }

    Date date1 = parse(format(d1, DEFAULT_DATE_FMT), DEFAULT_DATE_FMT);
    Date date2 = parse(format(d2, DEFAULT_DATE_FMT), DEFAULT_DATE_FMT);

    return (int) Math.abs((date1.getTime() - date2.getTime()) / 1000 );
  }

  /**
   * 计算月份
   * @param date
   * @param changeValue  正数为增加，负数为减去
   * @return
   */
  public static Date changeMonth(Date date , int changeValue) {
    if(date == null || changeValue == 0) {
      return date;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, changeValue);
    return c.getTime();
  }

  /**
   * 计算天
   * @param date
   * @param changeValue  正数为增加，负数为减去
   * @return
   */
  public static Date changeDay(Date date , int changeValue) {
    if(date == null || changeValue == 0) {
      return date;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DAY_OF_YEAR, changeValue);
    return c.getTime();
  }

  /**
   * 计算小时
   * @param date
   * @param changeValue  正数为增加，负数为减去
   * @return
   */
  public static Date changeHour(Date date , int changeValue) {
    if(date == null || changeValue == 0) {
      return date;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.HOUR_OF_DAY, changeValue);
    return c.getTime();
  }


  /**
   * 计算秒
   * @Title: changeMin
   * @param date
   * @param changeValue
   * @return Date
   * @throws
   */
  public static Date changeMin(Date date, int changeValue) {
    if (date == null || changeValue == 0) {
      return date;
    }
    long flag = changeValue * 60 * 1000;
    return new Date(date.getTime() + flag);
  }

  /**
   * @Title: getYear
   * @Description: 获取年份
   * @param date
   * @return int
   * @throws
   */
  public static int getYear(Date date){
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int year = c.get(Calendar.YEAR);

    return year;
  }

  /**
   * @Title: getMonth
   * @Description: 获取月份
   * @param date
   * @return int
   * @throws
   */
  public static int getMonth(Date date){
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int month = c.get(Calendar.MONTH) + 1;

    return month;
  }

  /**
   * @Title: getDay
   * @Description: 获日期
   * @param date
   * @return int
   * @throws
   */
  public static int getDay(Date date){
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int day = c.get(Calendar.DATE);

    return day;
  }

  public static Calendar date2Calendar(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c;
  }

  public static int getHour(Calendar c) {
    return c.get(Calendar.HOUR_OF_DAY);
  }

  public static int getMinute(Calendar c) {
    return c.get(Calendar.MINUTE);
  }


  /**
   * 获取date日期的月的第一天
   * @Title: getMonthBeginDate
   * @Description: 获取date日期的月的第一天
   * @param date
   * @return Date
   * @throws
   */
  public static Date getMonthBeginDate(Date date){
    Calendar cal = Calendar.getInstance();
    // 当前月的第一天
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    Date beginTime = cal.getTime();

    return beginTime;
  }

  /**
   * 获取date日期的月的最后一天
   * @Title: getMonthEndDate
   * @Description: 获取date日期的月的最后一天
   * @param date
   * @return Date
   * @throws
   */
  public static Date getMonthEndDate(Date date){
    Calendar cal = Calendar.getInstance();
    // 当前月的最后一天
    cal.setTime(date);
    cal.set(Calendar.DATE, 1);
    cal.roll(Calendar.DATE, -1);
    Date endTime = cal.getTime();

    return endTime;
  }

  /**
   * @author fxf
   * @since 2014-6-14 15:50:20
   * @Title isExpire
   * @Description:检验与当前时间相比是否在一定时间内，没有过期
   * @param date
   * @param seconds
   * @return
   */
  public static boolean isExpire(Date date,int seconds){
    boolean flag = false;
    if ((System.currentTimeMillis()- date.getTime()) < seconds * 1000){
      flag = true;
    }
    return flag;
  }

  /**
   * 两者日期的距离, 如果有一个为空，那么返回-1
   * @param date1
   * @param date2
   * @return
   */
  public static long distance(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      return -1L;
    }
    return Math.abs(date1.getTime() - date2.getTime());
  }
//	public static boolean isOpen(String startTime, String endTime, Date date) {
//		boolean flag = false;
//		String time = formatNoDateAndNoSecNoException(date);
//		if(Integer.valueOf(startTime.substring(0, 1)) < Integer.valueOf(time.substring(0, 1)) && Integer.valueOf(endTime.substring(0, 1)) > Integer.valueOf(time.substring(0, 1))) {
//			return true;
//		}
//		if( Integer.valueOf(startTime.substring(0, 1)) == Integer.valueOf(time.substring(0, 1)) && Integer.valueOf(startTime.substring(3, 4)) < Integer.valueOf(time.substring(3, 4))) {
//			return true;
//		}
//		if( Integer.valueOf(startTime.substring(0, 1)) == Integer.valueOf(time.substring(0, 1)) && Integer.valueOf(startTime.substring(3, 4)) < Integer.valueOf(time.substring(3, 4))) {
//			return true;
//		}
//		return flag;
//	}
  /**
   * 2个日期的间隔天数
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static int getDaysBetween(Date startDate, Date endDate) {
    Calendar fromCalendar = Calendar.getInstance();
    fromCalendar.setTime(startDate);
    fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
    fromCalendar.set(Calendar.MINUTE, 0);
    fromCalendar.set(Calendar.SECOND, 0);
    fromCalendar.set(Calendar.MILLISECOND, 0);

    Calendar toCalendar = Calendar.getInstance();
    toCalendar.setTime(endDate);
    toCalendar.set(Calendar.HOUR_OF_DAY, 0);
    toCalendar.set(Calendar.MINUTE, 0);
    toCalendar.set(Calendar.SECOND, 0);
    toCalendar.set(Calendar.MILLISECOND, 0);

    return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
  }
  public static boolean isOpen(Date startTime, Date endTime, Date date) {
    if((startTime.before(date) || startTime.equals(date) ) && (endTime.after(date) || endTime.equals(date))) {
      return true;
    }
    return false;
  }
  public static String[] genDateArray(Date d1, Date d2) {
    Date startDate = d1;
    Date endDate = d2;
    if (d1.compareTo(d2) > 0) {
      startDate = d2;
      endDate = d1;
    }
    int days = getDaysBetween(startDate, endDate);
    String[] dates = new String[days + 1];
    for (int i = 0; i <= days; i++) {
      if (i == 0) {
        dates[i] = DateUtil.format(startDate, DEFAULT_DATE_FMT_NO);
        continue;
      }
      dates[i] = DateUtil.format(changeDay(startDate, i), DEFAULT_DATE_FMT_NO);
    }

    return dates;
  }

  /**
   * 获取距离今天的前day天日期字符串
   *
   * @param day
   * @return
   */
  public static String getYesterday(int day) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, day);
    return DateUtil.format(cal.getTime(),DEFAULT_DATE_FMT_NO);
  }

  /**
   * 获取还有多少时间结束
   * @param end
   * @return
   */
  public static String getLeaveTime(Date end) {
    Long endTime = end.getTime();
    long leaveTime = endTime - System.currentTimeMillis();
    if (leaveTime > 0) {
      long day = leaveTime / (24 * 3600 * 1000);
      long hour = leaveTime / (3600 * 1000) - 24 * day;
      return "剩余：" + day + "天 " + hour + "时";
    } else {
      return "活动已结束";
    }
  }
}
