package com.hh.dataservice.mapper;

import com.hh.api.model.BidInfo;
import com.hh.api.pojo.BidInfoProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {
    /*累计成交金额*/
    BigDecimal selectSumBidMoney();
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo row);

    int insertSelective(BidInfo row);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo row);

    int updateByPrimaryKey(BidInfo row);

    /*某个产品的投资记录*/
    List<BidInfoProduct> selectByProductId(
            @Param("productId") Integer productId,
            @Param("offset") int offset,
            @Param("rows") Integer rows);

    List<BidInfo> selectByUid(@Param("uid") Integer uid, @Param("offset") int offset, @Param("rows") Integer rows);

    /*某个产品的投资记录*/
    List<BidInfo> selectByProId(@Param("productId") Integer productId);
}