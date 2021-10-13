package com.c1eye.server.util;

import com.c1eye.server.bo.PageCounter;

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
}
