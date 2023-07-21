package com.hh.front.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hh.sms")
public class SmsConfig {
    private String url;
    private String appKey;
    private String content;
    private String loginText;

    public SmsConfig() {
    }

    public SmsConfig(String url, String appKey, String content, String loginText) {
        this.url = url;
        this.appKey = appKey;
        this.content = content;
        this.loginText = loginText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLoginText() {
        return loginText;
    }

    public void setLoginText(String loginText) {
        this.loginText = loginText;
    }
}
