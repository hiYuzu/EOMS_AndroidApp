package com.a712.emos_androidapp.utils;

import android.content.Context;
import android.util.Log;

import com.a712.emos_androidapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo on 2017/7/6.
 */

public class FunctionUtil {

    /**
     * 判断是否故障
     *
     * @param type
     * @return
     */
    public static boolean isFault(String type) {
        boolean flag = false;
        if (type != null && (type.equals("501") || type.equals("502"))) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取故障任务标题名称
     *
     * @param context
     * @param type
     * @return
     */
    public static String getFtTitleName(Context context, String type) {
        String ftTitleName = "";
        switch (type) {
            case "501":
            case "502":
                ftTitleName = context.getString(R.string.main_fragment_content1);
                break;
            case "601":
                ftTitleName = context.getString(R.string.main_fragment_content2);
                break;
            case "602":
                ftTitleName = context.getString(R.string.main_fragment_content4);
                break;
            case "603":
                ftTitleName = context.getString(R.string.main_fragment_content3);
                break;
            case "604":
                ftTitleName = context.getString(R.string.main_fragment_content5);
                break;
            case "605":
                ftTitleName = context.getString(R.string.main_fragment_content6);
                break;
            default:
                break;
        }
        return ftTitleName;
    }

    /**
     * 判断是否巡检
     *
     * @param type
     * @return
     */
    public static boolean isDeviceInspect(String type) {
        boolean flag = false;
        if (type != null && type.equals("601")) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * dateTo比dateFrom多的天数是否在days内
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public static boolean differentDays(String dateFrom, String dateTo, int days) {
        boolean flag = false;
        try {
            int disDays = 0;
            if(dateFrom == null || dateFrom.isEmpty()){
                dateFrom = getNowDate();
            }
            if(dateTo == null || dateTo.isEmpty()){
                dateTo = getNowDate();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date2 = format.parse(dateTo);
            Date date1 = format.parse(dateFrom);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);

            int year1 = cal1.get(Calendar.YEAR);
            int year2 = cal2.get(Calendar.YEAR);
            if (year1 != year2)   //同一年
            {
                int timeDistance = 0;
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                    {
                        timeDistance += 366;
                    } else    //不是闰年
                    {
                        timeDistance += 365;
                    }
                }
                disDays = timeDistance + (day2 - day1);

            } else    //不同年
            {
                System.out.println("判断day2 - day1 : " + (day2 - day1));
                disDays = day2 - day1;
            }
            if (disDays <= days) {
                flag = true;
            }
        } catch (Exception e) {
            Log.e("FunctionUtil", "判断天数间隔异常，错误信息为：" + e.getMessage());
            flag = false;
        }
        return flag;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNowDate() {
        String temp_str = "";
        Date dt = new Date();
        //HH表示24小时制，如果换成hh表示12小时制,需要在最后追加aa表示上下午  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        temp_str = sdf.format(dt);
        return temp_str;
    }

}
