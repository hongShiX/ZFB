package com.hh.front.controller;

import com.hh.common.constants.RedisKey;
import com.hh.common.enums.RCode;
import com.hh.common.util.CommonUtil;
import com.hh.front.service.SmsService;
import com.hh.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1/sms")
public class SmsController extends BaseController{
    @Resource(name = "SmsCodeLoginImpl")
    private SmsService smsServiceLogin;

    @Resource(name = "SmsCodeRegisterImpl")
    private SmsService smsServiceRegister;
    /*发送注册验证码短信*/
    @GetMapping("/code/register")
    @ApiOperation("发送注册验证码")
    public RespResult sendCodeRegister(@RequestParam(value = "phone") String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            // 判断redis中是否有这个手机号的验证码
            String key = RedisKey.KEY_SMS_CODE_REG + phone;
            if (stringRedisTemplate.hasKey(key)) {
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            } else {
                boolean isSuccess = smsServiceRegister.sendSms(phone);
                if (isSuccess) {
                    result = RespResult.ok();
                }
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }

        return result;
    }

    /*发送登录验证码短信*/
    @GetMapping("/code/login")
    @ApiOperation("发送登录验证码")
    public RespResult sendCodeLogin(@RequestParam(value = "phone") String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            // 判断redis中是否有这个手机号的验证码
            String key = RedisKey.KEY_SMS_CODE_LOGIN  + phone;
            if (stringRedisTemplate.hasKey(key)) {
                result = RespResult.ok();
                result.setRCode(RCode.SMS_CODE_CAN_USE);
            } else {
                boolean isSuccess = smsServiceLogin.sendSms(phone);
                if (isSuccess) {
                    result = RespResult.ok();
                }
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }

        return result;
    }
}
