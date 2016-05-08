package com.example.ohyowan.notificationcallback.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Platform Development Center - NMP Corp.
 *
 * @author David KIM
 * @since 1.0
 */
public class CommonUtil {

    /**
    * 문자열이 null 이면 대치문자열로 대치
    *
    * @param str 문자열
    * @param replaceStr 대치 문자열
    * @return String
    */
    public static String nullToString(String str, String replaceStr) {
        return (str == null || "".equals(str))? replaceStr:str;
    }

   	/**
   	 * null 여부 체크
   	 *
   	 * @param str 문자열
   	 * @return boolean
   	 */
   	public static boolean isNull(String str) {
   		return (str == null || "".equals(str))? true:false;
   	}

   	/**
   	 * null 여부 체크
   	 *
   	 * @param str Object 객체
   	 * @return boolean
   	 */
   	public static boolean isNull(Object str) {
   		return (str == null || "".equals(str))? true:false;
   	}

   	/**
 	 * '(', ')' 문자 커버
 	 *
 	 * @param str Object 객체
 	 * @return boolean
 	 */
 	public static String setCover(String str) {
 		StringBuffer sb = new StringBuffer();

 		if(isNull(str)){
 			return "";
 		}
 		sb.append("(");
 		sb.append(str);
 		sb.append(")");
 		return sb.toString();
 	}

 	/**
 	 * '/' 분리선 등록
 	 *
 	 * @param str Object 객체
 	 * @return boolean
 	 */
 	public static String setSeperate(String org, String str) {
 		StringBuffer sb = new StringBuffer();

 		if(isNull(org)){
 			if(!isNull(str)){
 				sb.append(str);
 			}
 		}else{
 			sb.append(org);
 			if(!isNull(str)){
 				sb.append("/");
 		 		sb.append(str);
 			}
 		}
 		return sb.toString();
 	}

  	/**
 	 * 남여 가져오기
 	 *
 	 * @param
 	 * @return boolean
 	 */
 	public static String getSex(String sexCd) {
 		if(isNull(sexCd))
  			return "";

 		if(sexCd.equals("F")){
 	 		return "여";
 		}else{
 	 		return "남";
 		}
 	}

 	/**
 	 * 닉네임 한글 8, 영문 16자 확인
 	 *
 	 * @param str
 	 * @return
 	 */
 	public static boolean isNicknameSize(String str){
	 	 if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
	 		 // 한글 8자
	 		 if(str.length() < 9){
	 			 return true;
	 		 }
	 	 }else{
	 		 // 영문 16자
	 		 if(str.length() < 17){
	 			 return true;
	 		 }
	 	 }
	 	 return false;
 	}
	/**
	 * 문자 숨기기 .. 뒤에서 2자  **로 치환
	 * @param str
	 * @param re
	 * @return
	 */
	public static String hideString(String str, String re){
		String result = "";
		if(isNull(str))
			return str;

		if(str.length() == 1)
			return re;

		if(str.length() == 2)
			return str.substring(0, 1)+re;


		result = str.substring(0, str.length()-2)+re+re;
		return result;
	}

    /**
     * 문자열 특정기호로 나누기
     *
     * @param str String value
     * @param delim 구분자
     * @return List<String>
     */
    public static List<String> split2Iterate(String str, String delim) {
        List<String> result = new ArrayList<String>();

        if(isNull(str))
        	return result;

        StringTokenizer token = new StringTokenizer(str, delim);
        while (token.hasMoreTokens()) {
            result.add( token.nextToken() );
        }

        return result;
    }

    /**
     * 문자열 특정기호로 나누기
     *
     * @param str String value
     * @param delim 구분자
     * @return String[]
     */
    public static String[] split(String str, String delim) {
        StringTokenizer token = new StringTokenizer(str, delim);
        String[] result = new String[token.countTokens()];

        for (int i = 0; token.hasMoreTokens(); i++) {
            result[i] = token.nextToken();
        }

        return result;
    }

   	/**
   	 * 해당 요일 구하기
   	 *
   	 * @param date
   	 * @return
   	 */
   	public static String getDayOfWeek(String date) {
	    String result = "";

	    if(date == null || date.length() < 8) {
	        return result;
	    }

	    Calendar c = Calendar.getInstance();
        c.setTime(getDateMin(date.substring(0, 8)));

        switch(c.get(Calendar.DAY_OF_WEEK)) {
        	case Calendar.SUNDAY :
        	    result = "일";
        	    break;
        	case Calendar.MONDAY :
        	    result = "월";
        	    break;
        	case Calendar.TUESDAY :
        	    result = "화";
        	    break;
        	case Calendar.WEDNESDAY :
        	    result = "수";
        	    break;
        	case Calendar.THURSDAY :
        	    result = "목";
        	    break;
        	case Calendar.FRIDAY :
        	    result = "금";
        	    break;
        	case Calendar.SATURDAY :
        	    result = "토";
        	    break;
        }

        return result;
	}

	public static Date getDateMin(String date) {
		if (isNull(date)) return null;
		int len = date.length();
		if (len<4 || len>8) return null;

		int year  = toInt(date.substring(0, 4));
		int month = toInt(len>=6? date.substring(4, 6):"1");
		int day   = toInt(len>=8? date.substring(6, 8):"1");

		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * String -> int 변환
	 *
	 * @param arg String value
	 * @return int
	 */
	public static int toInt(String arg) { return Integer.parseInt(arg.trim()); }

	/**
	 * int -> String 변환
	 *
	 * @param arg int value
	 * @return String
	 */
	public static String toStr(int arg) { return Integer.toString(arg); }


   	/**
   	 * meassge 시간 가져오기
   	 *
   	 * @param
   	 * @return
   	 */
   	public static String massageTime(String str){
   		return massageTime(str, true);
   	}

   	public static String massageTime(String str, boolean isTime){
   		StringBuffer buf = new StringBuffer();

   		if(!isNull(str) && str.length() == 12){
   			int temp = Integer.parseInt(str.substring(8, 10));
   			if( temp == 0) {
   				buf.append("오전 ");
   				temp = 12;
   			}else if(temp == 12){
   				buf.append("오후 ");
   			}else if( temp > 12) {
   				temp = temp-12;
   				buf.append("오후 ");
   			}else{
   				buf.append("오전 ");
   			}
   			if(isTime)
   				buf.append(temp).append(":").append(str.substring(10, 12));
   		}
   		return buf.toString();
   	}

   	/**
   	 * 시스템 데이터 가져오기
   	 *
   	 * @return
   	 */
   	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("static-access")
	public static String getSysDate(int day) {
   		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmm");
   		Calendar cal = Calendar.getInstance();
   		cal.add(cal.DATE, day);

   		return date.format(cal.getTime());
   		//return matchFormat("yyyyMMddHHmm");
   	}

    /**
     *
     * 시스템 날짜 (yyyyMMdd)
     *
     * @return String
     */
     public static String getSysYearDay() { return matchFormat(new Date(), "yyyyMMdd"); }

	/**
	 *
	 * 시스템 날짜 (Calendar.YEAR)
	 *
	 * @return int
	 */
	public static int getSysYear() { return Calendar.getInstance().get(Calendar.YEAR); }

	/**
	 *
	 * 시스템 날짜 (Calendar.MONTH)
	 *
	 * @return String
	 */
	public static String getSysMonth() { return CommonUtil.toStr(Calendar.getInstance().get(Calendar.MONTH) + 1); }

     /**
     *
     * 시스템 날짜 Format
     *
     * @return String
     */
     public static String getSysDateFormat(String format) { return matchFormat(new Date(), format); }

    /**
     *
     * matchFormat
     *
     * @param dt 날짜
     * @param format Format
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
	public static String matchFormat(Date dt, String format) {
        if (dt == null) return "";
        try {
            return new SimpleDateFormat(format).format(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "invalid format";
    }

    /**
     *
     * 전화번호 정규식
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNo(String number) {
       String regex = "\\d{2,4}-\\d{3,4}-\\d{4}$";
       return number.matches(regex);
    }

    /**
     *
     * 이메일 정규식
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
        return email.matches(regex);
    }

    /**
     *
     * 숫자만 정규식
     *
      * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String regex = "[\\d]*$";
        return str.matches(regex);
    }


    /**
     *
     * 숫자 포함 여부
     *
      * @param str
     * @return
     */
    public static boolean isNumericInc(String str) {
		boolean isNumber = false;
        String regex = "[\\d]*$";

		String sub = "";
		for(int i=0; i<str.length(); i++){
			sub = str.substring(i, i+1);
			if(!isNumber)
				isNumber = sub.matches(regex);
		}

		if( isNumber ){
			return true;
		}else{
			return false;
		}
    }

    /**
     *
     * 특수문자 제거
     *
     * @param str
     * @return
     */
    public static String removeSpecialCharacter(String str) {
        String returnStr = str.replaceAll(";", "");
        returnStr = returnStr.replaceAll("'","");
        return returnStr;
    }

	/**
	 * 문자 숨기기 .. 뒤에서 2자  **로 치환
	 * @param str
	 * @param re
	 * @return
	 */
	public static String hideEmail(String str, String re){

		String result = "";
		if(isNull(str))
			return str;

		String[] arr = split(str, "@");
		if(isNull(arr) || arr.length < 2)
			return str;

		str = arr[0];
		if(str.length() == 1)
			return re;

		if(str.length() == 2)
			return str.substring(0, 1)+re;

		result = str.substring(0, str.length()-2)+re+re;


		return result+"@"+arr[1];
	}

	/**
	 * 금액 xxx,xxx 포맷
	 *
	 * @param arg String value
	 * @return String
	 */
	@SuppressLint("UseValueOf")
	public static String toCurFormat(int arg) {

		String str = String.valueOf(arg);
		Double db = new Double(str).doubleValue();

		return new DecimalFormat("###,##0").format(db);
	}
	/**
	 * 날짜
	 *
	 * @param datetime 날짜
	 * @return Date
	 * @throws Exception
	 */
	public static Date getDate(String datetime) throws Exception {
		String year, month, day, hour, minute, second;

		if (datetime==null ||
		    "".equals(datetime) ||
		    datetime.length() != 14) return null;

		year = datetime.substring(0, 4);
		month = datetime.substring(4, 6);
		day = datetime.substring(6, 8);
		hour = datetime.substring(8, 10);
		minute = datetime.substring(10, 12);
		second = datetime.substring(12, 14);

		return getDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
	}

	/**
     *
	 * 날짜
	 *
	 * @param year 년도
	 * @param month 월
	 * @param day 일
	 * @param hour 시
	 * @param minute 분
	 * @param second 초
	 * @return Date
	 */
	public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
    * targetDate 까지의 남은 시간을 일-시-분-초 순서로 배열로 반환한다.
    *
    * @param targetDate
    * @return int[] 일-시-분-초
    */
	public static int[] getIntervalSecond(Date targetDate) {
		return getIntervalSecond(targetDate, 3);

	}

	public static int[] getIntervalSecond(Date targetDate, int delay) {
	   Date sysdate = Calendar.getInstance().getTime();

	   int totalSec = (int)((targetDate.getTime() - sysdate.getTime()) / 1000  - delay );

	   int[] days = new int[4];
	   if(totalSec < 0)
		   totalSec = 0;

       int day = totalSec / (60 * 60 * 24);
       int hour = (totalSec - day * 60 * 60 * 24) / (60 * 60);
       int minute = (totalSec - day * 60 * 60 * 24 - hour * 60 * 60) / 60;
       int second = totalSec % 60;

       days[0] = day;
       days[1] = hour;
       days[2] = minute;
       days[3] = second;
       return days;
	}

   /**
    * 0 추가
    *
    * @param number 숫자
    * @param order 순서
    * @return String
    */
   public static String addZero(int number, int order) {
       return addZero(String.valueOf(number), order);
   }

   /**
    * 0 추가
    *
    * @param xxx 숫자
    * @param x 순서
    * @return String
    */
   public static String addZero(String xxx, int x) {
       StringBuffer yyy = new StringBuffer("");
       if (!(xxx == null || xxx.trim().equals(""))) {
           if(xxx.length() < x){
               for(int a = 0; a < x - xxx.length() ; a++){
                   yyy.append("0");
               }
               yyy.append(xxx);
           }
           else if(xxx.length() == x){
			   yyy.append(xxx.substring(x-xxx.length(),x));
           }
		   else if(xxx.length() > x){
			   yyy.append(xxx.substring(x-xxx.length(),xxx.length()));
		   }
           else {
               yyy.append(xxx);
           }
       }

       return yyy.toString();
   }

   /**
	 *
	 * String(yyyyMMdd) -> Date 변환
	 *
	 * @param arg String value
	 * @return Date
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date toDate(String arg){
		Date date = null;
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			date = transFormat.parse(arg);
		} catch (ParseException e) {}
		return date;
	}

   /**
	 *
	 * String(format) -> Date 변환
	 *
	 * @param arg String value
	 * @return Date
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date toDateFormat(String arg, String format){
		Date date = null;
		SimpleDateFormat transFormat = new SimpleDateFormat(format);
		try {
			date = transFormat.parse(arg);
		} catch (ParseException e) {}
		return date;
	}
	/**
	 *
	 * Date -> String(yyyyMMdd) 변환
	 *
	 * @param arg Date value
	 * @return String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String DateToStr(Date arg){
		String date = "";
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			date = transFormat.format(arg);
		} catch (Exception e) {}
		return date;
	}

	/**
	 *
	 * 두 시간의 간격
	 *
	 * @param d1 시작날짜
	 * @param d2 끝날짜
	 * @return int (분)
	 * @throws Exception
	 */
	public static int getTimeInterval(Date d1, Date d2) throws Exception {
	    int interval = 0;

	    interval = (int)((d1.getTime() - d2.getTime()) / (60 * 1000));

	    return interval;

	}

	/**
	 *
	 * 두 일자의 간격
	 *
	 * @param Date1
	 * @param Date2
	 * @return
	 * @throws Exception
	 */
	public static int getDateInterval(String Date1, String Date2) throws Exception {

		Date d1 = toDate(Date1);
		Date d2 = toDate(Date2);

		int interval = getTimeInterval(d1, d2);
		interval = interval / (60*24); // 일 단위로 변환

		return interval;
	}

	/**
	 * 오늘이 해당 기간에 유효한지 확인
	 *
	 * @param start (yyyyMMdd)
	 * @param end	(yyyyMMdd)
	 * @return
	 */
	public static boolean isWithInPeriod(String start, String end){

		boolean is = false;
		try{
			String today = getSysYearDay();
			int period_s = getDateInterval(today, start);
			int period_e = getDateInterval(today, end);
			if( period_s >= 0 && period_e <= 0){
				is = true;
			}
		}catch (Exception e) {
		}
		return is;
	}

	/**
	 * 해당 시간대 확인
	 *
	 * @param start	(HH)
	 * @param end	(HH)
	 * @return
	 */
	public static boolean isWithInPeriodTime(String start, String end){

		if(!isNull(start) && start.equals("*")){
			return true;
		}

		boolean is = false;
		try{
			String today = getSysYearDay();

			Date todayDate = toDateFormat(getSysDateFormat("yyyyMMddHH"), "yyyyMMddHH");
			Date startDate = toDateFormat(today+start, "yyyyMMddHH");
			Date endDate = toDateFormat(today+end, "yyyyMMddHH");

			int period_s = getTimeInterval(todayDate, startDate);
			int period_e = getTimeInterval(todayDate, endDate);
			if( period_s >= 0 && period_e <= 0){
				is = true;
			}
		}catch (Exception e) {
		}
		return is;
	}

	/**
	 * 요일 체크
	 * @param week
	 * @return
	 */
	public static boolean isWithInPeriodWeek(String week){

		if(!isNull(week) && week.equals("*")){
			return true;
		}

		boolean is = false;
		Calendar c = Calendar.getInstance();
        String str = toStr(c.get(Calendar.DAY_OF_WEEK));

        if(str.equals(week)){
        	is = true;
        }
        return is;
	}


	/**
	 *
     * MD5 변환
     *
     * @return String
     */
	public static String md5(String str){

		String re = null;
		if(isNull(str))
			return "";

		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());

			byte[] md5Bytes = md5.digest();
	        StringBuffer sb = new StringBuffer();
	        for(int i = 0 ; i < md5Bytes.length ; i++){
	                sb.append(Integer.toString((md5Bytes[i] & 0xf0) >> 4, 16));
	                sb.append(Integer.toString(md5Bytes[i] & 0x0f, 16));
	        }
	        re = sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}

		return re;
    }

    /**
     *
     * 알림 노티피케이션
     *
     * @param context 컨텍스트
     * @param title 제목
     * @param message 내용
     * @param icon 아이콘
     * @param banner 이미지
     */
    public static void notificationWithBigPicture(Context context, String title, String message, int icon, Bitmap banner) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
           .setSmallIcon(icon)
           .setTicker(title)
           .setContentTitle(title)
           .setContentText(message)
           .setAutoCancel(true);

        NotificationCompat.Style style = null;

        if(!CommonUtil.isNull(banner)) {
            NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
            bigStyle.setBigContentTitle(title);
            bigStyle.setSummaryText(message);
            bigStyle.bigPicture(banner);
            style = bigStyle;
        }

        builder.setStyle(style);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getNotifyID(), builder.build());
    }

    /**
     *
     * 알림 노티피케이션 (이용자가 Notification을 선택하면 특정 액티비티를 실행하기 위한 설정)
     *
     * @param context 컨텍스트
     * @param title 제목
     * @param message 내용
     * @param icon 아이콘
     * @param banner 이미지
     * @param activityClass 이동 액티비티
     */
    public static void notificationWithBigPicture(Context context, String title, String message, int icon, Bitmap banner, Class<?> activityClass) {

        int notifyID = getNotifyID();
        Intent intent = new Intent(context, activityClass);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notifyID", notifyID);

        // Log.e(TAG_WIFI, "notificationWithBigPicture - notifyID : " + notifyID);
        // 두번째 파라미터인 requestCode 가 달라야 함.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
               .setSmallIcon(icon)
               .setTicker(title)
               .setContentTitle(title)
               .setContentText(message)
               .setAutoCancel(true);

        NotificationCompat.Style style = null;

        if(!CommonUtil.isNull(banner)) {
            NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
            bigStyle.setBigContentTitle(title);
            bigStyle.setSummaryText(message);
            bigStyle.bigPicture(banner);
            style = bigStyle;
        }

        builder.setStyle(style);
        builder.setContentIntent(pendingIntent);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, builder.build());
    }

    /**
     *
     * 알림 노티피케이션 (이용자가 Notification을 선택하면 웹브라우저를 실행하기 위한 설정)
     *
     * @param context 컨텍스트
     * @param title 제목
     * @param message 내용
     * @param icon 아이콘
     * @param banner 이미지
     * @param uri 이동 uri
     */
    public static void notificationWithBigPicture(Context context, String title, String message, int icon, Bitmap banner, String uri) {

        int notifyID = getNotifyID();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notifyID", notifyID);

        // Log.e(TAG_WIFI, "notificationWithBigPicture - notifyID : " + notifyID);
        // 두번째 파라미터인 requestCode 가 달라야 함.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
               .setSmallIcon(icon)
               .setTicker(title)
               .setContentTitle(title)
               .setContentText(message)
               .setAutoCancel(true);

        NotificationCompat.Style style = null;

        if(!CommonUtil.isNull(banner)) {
            NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
            bigStyle.setBigContentTitle(title);
            bigStyle.setSummaryText(message);
            bigStyle.bigPicture(banner);
            style = bigStyle;
        }

        builder.setContentIntent(pendingIntent);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, builder.build());
    }

    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getNotifyID() {
        return c.incrementAndGet();
    }

    public static String removeMarkText(String str){
        str = str.replace("\"", "");
        return str;
    }

    public static String getSecondToDate(int totalSec) {

        int day = totalSec / (60 * 60 * 24);
        int hour = (totalSec - day * 60 * 60 * 24) / (60 * 60);
        int minute = (totalSec - day * 60 * 60 * 24 - hour * 3600) / 60;
        int second = totalSec % 60;

        StringBuilder sb = new StringBuilder();
        if (day != 0) {
            sb.append(day);
            sb.append("일 ");
        }

        if (hour != 0) {
            sb.append(hour);
            sb.append("시간 ");
        }

        if (minute != 0) {
            sb.append(minute);
            sb.append("분 ");
        }

        if (day == 0 && hour == 0 && minute == 0 && second != 0) {
            sb.append(second);
            sb.append("초");
        }

        return sb.toString();
    }

	/**
	 *
	 * 쌈따옴표를 제거한다.
	 *
	 * @param string
	 * @return
	 */
	public static String removeQuotedString(String string) {

        if(isNull(string)) {
			return "";
		}

        String result = string.trim().replaceAll("^\"|\"$", "");
        return result.trim();
	}

    /**
     *
     * 안드로이드 아이디 가져오기
     *
     * @param ctx
     * @return
     */
    /*public static String getAndroidId(Context ctx){

        String androidId = SharedPreferenceUtil.getStringSharedPreference(ctx, CONS_PREFS_AP_INFO, "androidId");

        if(isNull(androidId)){
            try{
                androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
                SharedPreferenceUtil.putStringSharedPreference(ctx, CONS_PREFS_AP_INFO, "androidId", androidId);
            }catch (Exception e) {

            }
        }

        return CommonUtil.nullToString(androidId, "");
   }*/

	/**
	 *
	 * 문자열 사이에 값을 넣는다.
	 *
	 * @param str 값
	 * @param appendValue 추가할 문자
	 * @param index 인덱스
	 * @return String
	 */
    public static String appendValue(String str, String appendValue, int index) {
		StringBuffer sb = new StringBuffer(str);
		try {
			sb.insert(index, appendValue);
		} catch (Exception e) {
			return "";
		}

		return sb.toString();
	}

    /**
     *
     * 로그
     *
     * @param level 로그레벨
     * @param debugMessage 로그메세지
     */
    public static void wifiLog(String level, String debugMessage) {
        boolean isDebug = true;
        if(isDebug || level.equalsIgnoreCase("error")) {
            Log.v("WIFI", debugMessage);
        }
    }

	/**
	 * 어플 실행을 위한 Intent 생성
	 * 없으면 Makget으로 연결
	 *
	 * @param mContext 컨텍스트
	 * @param packageName 패키지명
	 * @return Intent 명
	 */
	public static Intent onExecuteApp(Context mContext, String packageName){
		final PackageManager pkManager = mContext.getPackageManager();
		List<ApplicationInfo> packages = pkManager.getInstalledApplications(PackageManager.GET_META_DATA);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));

		for(ApplicationInfo packageInfo : packages) {
			if(packageName.equals(packageInfo.packageName)){
				return new Intent(pkManager.getLaunchIntentForPackage(packageInfo.packageName));
			}
		}
		return intent;
	}

	/**
	 *
	 * 어플의 설치 여부를 결정한다.
	 *  true : 설치됨
	 *  false : 설치안됨
	 *
	 * @param mContext 컨텍스트
	 * @param packageName 패키지명
	 * @return boolean
	 */
	public static boolean isInstallApp(Context mContext, String packageName) {
		final PackageManager pkManager = mContext.getPackageManager();
		List<ApplicationInfo> packages = pkManager.getInstalledApplications(PackageManager.GET_META_DATA);
		boolean isInstall = false;
		for(ApplicationInfo packageInfo : packages) {
			if(packageName.equals(packageInfo.packageName)){
				isInstall = true;
			}
		}
		return isInstall;
	}

}