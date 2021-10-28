package com.c1eye.server.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * @author c1eye
 * time 2021/10/23 10:13
 */
@Component
public class OrderUtil {
    // B3230651812529
    private static String[] yearCodes;

    @Value("${c1eye.year-codes}")
    public void setYearCodes(String yearCodes) {
        String[] chars = yearCodes.split(",");
        OrderUtil.yearCodes = chars;
    }

    public static String makeOrderNo() {
        StringBuffer joiner = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        String mills = String.valueOf(calendar.getTimeInMillis());
        String micro = LocalDateTime.now().toString();
        String random = String.valueOf(Math.random()*1000).substring(0,2);
        joiner.append(OrderUtil.yearCodes[calendar.get(Calendar.YEAR) - 2021])//A的起始年份
              .append(Integer.toHexString(calendar.get(Calendar.MONTH)+1).toUpperCase())
              .append(calendar.get(Calendar.DAY_OF_MONTH))
              .append(mills.substring(mills.length()-5, mills.length()))
              .append(micro.substring(micro.length()-3, micro.length()))
              .append(random);
        return joiner.toString();

    }


}
