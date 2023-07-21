package com.hh.api.service;

import com.hh.api.pojo.BaseInfo;

public interface PlatBaseInfoService {
    /**
     * 计算利率，注册人数，累计成交金额
     */
    BaseInfo queryPlatBaseInfo();
}
