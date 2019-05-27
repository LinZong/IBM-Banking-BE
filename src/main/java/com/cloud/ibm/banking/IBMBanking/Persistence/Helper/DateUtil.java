package com.cloud.ibm.banking.IBMBanking.Persistence.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    static String format1 = "yyyy-MM-dd HH:mm:ss";
     //时间戳转换成日期格式字符串
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

     //日期格式字符串转换成时间戳
    public static String date2TimeStamp(String date_str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
     //取得当前时间戳（精确到秒）
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }
}
