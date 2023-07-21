package com.hh.common.enums;

public enum RCode {
    UNKOWN(0, "请稍后重试"),
    SUCCESS(1000, "请求成功"),
    REQUEST_PRODUCT_TYPE_ERR(1001, "产品类型有误"),
    PRODUCT_OFFLINE(1002, "产品已经下线"),

    REQUEST_PARAM_ERR(1003, "参数不正确"),
    PHONE_FORMAT_ERR(1004, "手机号格式不正确"),
    PHONE_EXISTS(1005, "手机号已存在"),

    SMS_CODE_CAN_USE(1006, "验证码可以继续使用"),
    SMS_CODE_INVALID(1007, "验证码无效"),
    PHONE_LOGIN_PASSWORD_INVALID(1008, "手机号或者密码无效"),
    IDENTIFY_FAIL(1009, "实名认证无效"),
    IDENTIFY_REPEAT(1010, "已经实名认证"),
    TOKEN_INVALID(3000, "token无效"),


    ;
    /**
     * 应答码
     * 0 默认
     * 1000-2000 请求参数有误
     * 2000-3000 服务器请求错误
     * 3000-4000 访问dubbo错误
     */
    private int code;

    private String txt;

    RCode(int c, String t) {
        this.code = c;
        this.txt = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
