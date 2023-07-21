package com.hh.api.service;

import com.hh.api.model.BidInfo;

import java.util.List;

public interface BidInfoService {
    List<BidInfo> queryByUid(Integer uid, Integer pageNo, Integer pageSize);
}
