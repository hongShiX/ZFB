package com.hh.dataservice.service;

import com.hh.api.pojo.BaseInfo;
import com.hh.api.service.PlatBaseInfoService;
import com.hh.dataservice.mapper.BidInfoMapper;
import com.hh.dataservice.mapper.ProductInfoMapper;
import com.hh.dataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;

@DubboService(interfaceClass = PlatBaseInfoService.class, version = "1.0-SNAPSHOT")
public class PlatBaseInfoServiceImpl implements PlatBaseInfoService {
    /*注入mapper*/
    @Resource
    private UserMapper userMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;

    /*平台基本信息*/
    @Override
    public BaseInfo queryPlatBaseInfo() {
        /*获取注册人数，收益率平均值，累计成交金额*/

        /*注册人数*/
        int registerUser = userMapper.selectCountUsers();

        /*收益率平均值*/
        BigDecimal avgRate = productInfoMapper.selectAvgRate();

        /*累计成交金额*/
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();

        return new BaseInfo(avgRate, sumBidMoney, registerUser);
    }
}
