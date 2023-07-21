package com.hh.dataservice.service;

import com.hh.api.model.ProductInfo;
import com.hh.api.pojo.MutiProduct;
import com.hh.api.service.ProductService;
import com.hh.common.constants.ZFBConstant;
import com.hh.common.util.CommonUtil;
import com.hh.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class, version = "1.0-SNAPSHOT")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductInfoMapper productInfoMapper;

    /*按类型分页查询产品*/
    @Override
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {
        List<ProductInfo> productInfos = new ArrayList<>();
        if (pType == 0 || pType == 1 || pType == 2) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageNo(pageSize);
            int offset = (pageNo - 1) * pageSize;
            productInfos = productInfoMapper.selectByTypeLimit(pType, offset, pageSize);
        }
        return productInfos;
    }

    @Override
    public MutiProduct queryIndexPageProducts() {
        MutiProduct result = new MutiProduct();
        /*查询新手宝*/
        List<ProductInfo> xinShouBaoList = productInfoMapper.selectByTypeLimit(ZFBConstant.PRODUCT_TYPE_XINSHOUBAO, 0, 1);

        /*查询优选*/
        List<ProductInfo> youXuanList = productInfoMapper.selectByTypeLimit(ZFBConstant.PRODUCT_TYPE_YOUXUAN, 0, 3);

        /*查询散标*/
        List<ProductInfo> sanBiaoList = productInfoMapper.selectByTypeLimit(ZFBConstant.PRODUCT_TYPE_SANBIAO, 0, 3);

        result.setXinShouBao(xinShouBaoList);
        result.setYouXuan(youXuanList);
        result.setSanBiao(sanBiaoList);

        return result;
    }

    /*某个类型产品的记录总数*/
    @Override
    public Integer queryRecordNumsByType(Integer pType) {
        Integer count = 0;
        if (pType == 0 || pType == 1 || pType == 2) {
            count = productInfoMapper.selectCountByType(pType);
        }
        return count;
    }

    /*根据产品id查询产品信息*/
    @Override
    public ProductInfo queryById(Integer id) {
        ProductInfo productInfo =  null;
        if (id != null && id > 0) {
            productInfo = productInfoMapper.selectByPrimaryKey(id);
        }
        return productInfo;
    }
}
