package com.hh.api.service;

import com.hh.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {
    /*根据userId查询它的充值记录*/
    List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize);


    /**/
    int addRechargeRecord(RechargeRecord record);

    /*处理后续充值*/
    int handlerKQNotify(String orderId, String payAmount, String payResult);
}
