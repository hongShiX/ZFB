package com.hh.front.service;

public interface SmsService {
    /**
     * @param phone 手机号
     * @return true 发送成功 false 其他情况
     */
    boolean sendSms(String phone);

    /**
     * 验证短信验证码
     * @param phone 手机号
     * @param code 短信验证码
     * @return true验证成功 false验证失败
     */
    boolean checkSmsCode(String phone, String code);
}
