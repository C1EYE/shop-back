package com.c1eye.server.util;

import com.c1eye.server.bo.PageCounter;

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
}
