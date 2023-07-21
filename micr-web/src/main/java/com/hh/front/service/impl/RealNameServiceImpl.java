package com.hh.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hh.api.service.UserService;
import com.hh.common.util.HttpClientUtils;
import com.hh.front.config.RealNameConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service

public class RealNameServiceImpl {
    @Resource
    private RealNameConfig realNameConfig;

    @DubboReference(interfaceClass = UserService.class, version = "1.0-SNAPSHOT")
    private UserService userService;
    public boolean handleRealName(String phone, String name, String idCard) {
        boolean isReal = false;
        String url = realNameConfig.getUrl();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + realNameConfig.getAppKey());
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("idNo", idCard);
        try {
            String res = HttpClientUtils.doPost(url, headers, params);
            if (StringUtils.isNotBlank(res)) {
                JSONObject resObject = JSONObject.parseObject(res);
                if ("0000".equalsIgnoreCase(resObject.getString("respCode"))) {

                    isReal = true;
                    // 处理更新数据库
                    boolean modifyResult = userService.modifyRealName(phone, name, idCard);
                    isReal = modifyResult;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isReal;
    }
}
