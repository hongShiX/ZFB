package com.hh.common.constants;

/**
 * 常量类
 */
public class ZFBConstant {
    /********************产品类型**************************/

    /*新手宝*/
    public static final int PRODUCT_TYPE_XINSHOUBAO = 0;
    /*优选*/
    public static final int PRODUCT_TYPE_YOUXUAN = 1;
    /*散标*/
    public static final int PRODUCT_TYPE_SANBIAO = 2;

    /******产品状态*****/
    /*未满标*/
    public static final int PRODUCT_STATUS_SELL = 0;
    /*满标*/
    public static final int PRODUCT_STATUS_SELLED= 1;
    /*收益计划*/
    public static final int PRODUCT_STATUS_PLAN= 2;


    /********投资状态*/
    /*投资成功*/
    public static final int INVEST_STATUS_SUCCESS = 1;

    /*投资失败*/
    public static final int INVEST_STATUS_FAIL = 2;


    /*********收益状态*/
    /*生成收益计划*/
    public static final int INCOME_STATUS_PLAN = 0;
    /*收益返还*/
    public static final int INCOME_STATUS_BACK = 1;


    /*******充值状态******/

    /*充值中*/
    public static final int RECHARGE_STATUS_PROCESSING = 0;

    /*充值成功*/
    public static final int RECHARGE_STATUS_SUCCESS = 1;

    /*充值失败*/
    public static final int RECHARGE_STATUS_FAIL = 2;
}
