package com.hh.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.common.constants.RedisKey;
import com.hh.front.config.SmsConfig;
import com.hh.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * 注册短信发送验证码
 */
@Service("SmsCodeRegisterImpl")
public class SmsCodeRegisterImpl implements SmsService {
    @Resource
    private SmsConfig smsConfig;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean sendSms(String phone) {
        boolean send = false;
        // 设置短信内容
        String random = RandomStringUtils.randomNumeric(4);
        System.out.println("注册验证码：" + random);

        // 更新content中的%s
        String content = String.format(smsConfig.getContent(), random);

        // 使用httpClient发送get请求给第三方
        String url = smsConfig.getUrl() + "?mobile=" + phone
                + "&content="+ content
                +"&appkey=" + smsConfig.getAppKey();
        URL u = null;
        URI uri = null;
        try {
            u = new URL(url);
            uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);

        try {
            CloseableHttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // String txt = EntityUtils.toString(response.getEntity());

                // 由于短信接口已经停用，这里我模拟短信发送，在后台查看短信验证码
                String txt = "{\"code\": \"10000\", \"result\" : {\"ReturnStatus\": \"Success\"}}";

                // 解析json
                if (StringUtils.isNoneBlank(txt)) {
                    // fastJson
                    JSONObject jsonObject = JSONObject.parseObject(txt);
                    if ("10000".equals(jsonObject.getString("code"))) {
                        // 第三方接口调用成功
                        // 读取result中的key，content
                        if ("Success".equals(jsonObject.getJSONObject("result").getString("ReturnStatus"))) {
                            send = true;

                            // 短信验证码存入到redis内
                            String key = RedisKey.KEY_SMS_CODE_REG + phone;
                            // 将验证码存入redis中，有效期三分钟
                            stringRedisTemplate.boundValueOps(key).set(random, 3, TimeUnit.MINUTES);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return send;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        String key = RedisKey.KEY_SMS_CODE_REG + phone;
        if (stringRedisTemplate.hasKey(key)) {
            String querySmsCode = stringRedisTemplate.boundValueOps(key).get();
            if (code.equals(querySmsCode)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
