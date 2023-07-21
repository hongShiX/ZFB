package com.hh.front.vo;

/**
 * 登录用到的实体类
 */
public class LoginInfo {
    /*手机号*/
    private String phone;
    /*密码*/
    private String pword;
    /*验证码*/
    private String scode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }
}
