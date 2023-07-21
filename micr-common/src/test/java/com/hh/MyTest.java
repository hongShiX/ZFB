package com.hh;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class MyTest {
    @Test
    public void test01() {
        Date cur = new Date();
        System.out.println("cur " + cur);

        Date date = DateUtils.addDays(cur, -1);
        System.out.println("yesterday " + date);

        // 0点时间
        Date truncate = DateUtils.truncate(date, Calendar.DATE);
        System.out.println(truncate);
    }
}
