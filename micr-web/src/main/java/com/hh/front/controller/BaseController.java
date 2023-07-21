package com.hh.front.controller;

import com.hh.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

public class BaseController {
    /*声明公共资源*/

    /**/
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    /*平台信息服务*/
    @DubboReference(interfaceClass = PlatBaseInfoService.class, version = "1.0-SNAPSHOT")
    protected PlatBaseInfoService platBaseInfoService;

    /*产品服务*/
    @DubboReference(interfaceClass = ProductService.class, version = "1.0-SNAPSHOT")
    protected ProductService productService;

    @DubboReference(interfaceClass = InvestService.class, version = "1.0-SNAPSHOT")
    protected InvestService investService;

    @DubboReference(interfaceClass = UserService.class, version = "1.0-SNAPSHOT")
    protected UserService userService;

    @DubboReference(interfaceClass = RechargeService.class, version = "1.0-SNAPSHOT")
    protected RechargeService rechargeService;

    @DubboReference(interfaceClass = BidInfoService.class, version = "1.0-SNAPSHOT")
    protected BidInfoService bidInfoService;

}
