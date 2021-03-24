package com.example.cli.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wjw
 */
public class DateUtils {

    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(null == date ? new Date() : date);
    }

    /**
     * 获取订单编号
     *
     * @return
     */
    public synchronized static String getOrderNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        //同1s内可能有同号的,可以不用随机数而用有序数列,但批量导入递增订单号就有问题
        return format + RandomStringUtils.randomNumeric(5);
    }

    /**
     * 获得本月第一天0点时间
     *
     * @return
     */
    public static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    public static void main(String[] args) {
//        System.out.println(MD5Utils.stringToMD5("123456"));
//        System.out.println(getOrderNo());
//        System.out.println(getTimesMonthmorning());
    }
}
