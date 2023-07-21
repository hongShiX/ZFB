package com.hh.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class CommonUtil {
    private CommonUtil() {

    }

    /*处理pageNo*/
    public static int defaultPageNo(Integer pageNo) {
        int pNo = pageNo;
        if (pageNo == null || pageNo < 1) {
            pNo = 1;
        }
        return pNo;
    }

    /*处理pageSize*/
    public static int defaultPageSize(Integer pageSize) {
        int pSize = pageSize;
        if(pageSize == null || pageSize < 1) {
            pSize = 1;
        }
        return pSize;
    }

    /*手机号脱敏*/
    public static String tuoMinPhone(String phone) {
        String result = "***********";
        if (phone != null && phone.trim().length() == 11) {
            result = phone.substring(0,3) + "******" + phone.substring(9, 11);
        }
        return result;
    }

    /*验证手机号是否合法（中国大陆）true合法，false不合法*/
    public static Boolean checkPhone(String phone) {
        boolean flag = false;
        if (phone != null && phone .length() == 11) {
            flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return flag;
    }

    /*比较BigDecimal n1 >= n2为true，反之为false*/
    public static boolean ge(BigDecimal n1, BigDecimal n2) {
        if (n1 == null || n2 == null) {
            throw new RuntimeException("参数BigDecimal为null");
        }
        return n1.compareTo(n2) >= 0;
    }
}
