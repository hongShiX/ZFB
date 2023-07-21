package com.hh.dataservice.mapper;

import com.hh.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {
    /*利率平均值*/
    BigDecimal selectAvgRate();



    /*按产品类型分页查询*/
    List<ProductInfo> selectByTypeLimit(
            @Param("type") Integer pType,
            @Param("offset") Integer offset,
            @Param("rows") Integer rows
    );

    /*某个类型产品的总数*/
    Integer selectCountByType(@Param("pType") Integer pType);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo row);

    int insertSelective(ProductInfo row);

    ProductInfo selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(ProductInfo row);

    int updateByPrimaryKey(ProductInfo row);


    /*扣除产品剩余可投资金额*/
    int updateLeftProductMoney(@Param("id") Integer productId, @Param("money") BigDecimal money);

    /*更新产品为满标*/

    int updateSelled(@Param("id") Integer productId);

    /*满标的理财数据列表*/
    List<ProductInfo> selectFullTimeProduct(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /*更新产品状态*/
    int updateStatus(@Param("productId") int productId, @Param("status") int status);
}