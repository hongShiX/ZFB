package com.hh.common.constants;

public class RedisKey {
    /*投资排行榜*/
    public static final String KEY_INVEST_RANK = "INVEST:RANK";

    /*注册时短信验证码 SMS:REG:手机号*/
    public static final String KEY_SMS_CODE_REG = "SMS:REG:";

    /*登陆时短信验证码 SMS:LOGIN:手机号*/
    public static final String KEY_SMS_CODE_LOGIN = "SMS:LOGIN:";

    /*redis自增*/
    public static final String KEY_RECHARGE_ORDER_ID = "RECHARGE:ORDERID:SEQ";

    /*orderId*/
    public static final String KEY_ORDERID_SET = "RECHARGE:ORDERID:SET";
}
