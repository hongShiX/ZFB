package com.hh.api.service;

import com.hh.api.model.ProductInfo;
import com.hh.api.pojo.MutiProduct;

import java.util.List;

public interface ProductService {
    /*根据产品类型进行分页查询*/
    List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize);

    /*首页多个产品数据*/
    MutiProduct queryIndexPageProducts();

    /*某个类型产品的记录总数*/
    Integer queryRecordNumsByType(Integer pType);

    /*根据产品id查询产品信息*/
    ProductInfo queryById(Integer id);
}
