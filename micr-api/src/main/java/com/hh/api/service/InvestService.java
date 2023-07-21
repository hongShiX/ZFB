package com.hh.api.service;

import com.hh.api.pojo.BidInfoProduct;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {
    /*查询某个产品的投资记录*/
    List<BidInfoProduct> queryBidListByproductId(Integer productId, Integer pageNo, Integer pageSize);

    /*投资理财产品，1投资成功*/
    Integer investProduct(Integer uid, Integer productId, BigDecimal money);
}
