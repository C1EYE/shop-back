package com.c1eye.server.util;

import com.c1eye.server.bo.PageCounter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * @author c1eye
 * time 2021/10/10 10:28
 */
public class CommonUtils {
    public static PageCounter convertToPageParameter(Integer start,Integer count){
        int pageNum = start / count;
        PageCounter pageCounter = PageCounter.builder().page(pageNum).count(count).build();
        return pageCounter;
    }

    public static Boolean isInTimeLine(Date date, Date start, Date end) {
        Long time = date.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        if (time > startTime && time < endTime) {
            return true;
        }
        return false;
    }
    public static Calendar addSomeSeconds(Calendar calendar, int seconds){
        calendar.add(Calendar.SECOND, seconds);
        return calendar;
    }

    /**
     * 是否过期
     * @param startTime
     * @param period
     * @return
     */
    public static Boolean isOutOfDate(Date startTime,Long period){
        Long now = Calendar.getInstance().getTimeInMillis();
        Long currentTimeStamp = startTime.getTime();
        Long periodMileSecond = period * 1000;
        if(now > (currentTimeStamp + periodMileSecond)){
            return true;
        }
        return false;
    }

    public static Boolean isOutOfDate(Date expiredTime){
        long now = Calendar.getInstance().getTimeInMillis();
        long expired = expiredTime.getTime();
        if(now > expired){
            return true;
        }
        return false;

    }

    public static String yuanToFenPlainString(BigDecimal p){
        p = p.multiply(new BigDecimal("100"));
        return CommonUtils.toPlain(p);

    }

    public static String timestamp10(){
        long timestamp13 = Calendar.getInstance().getTimeInMillis();
        String timestamp13Str = timestamp13+"";
        return timestamp13Str.substring(0, timestamp13Str.length() - 3);
    }

    private static String toPlain(BigDecimal p) {
        return p.stripTrailingZeros().toPlainString();
    }

}
