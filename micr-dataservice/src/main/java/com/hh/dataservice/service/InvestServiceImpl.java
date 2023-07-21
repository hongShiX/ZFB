package com.hh.dataservice.service;

import com.hh.api.model.BidInfo;
import com.hh.api.model.FinanceAccount;
import com.hh.api.model.ProductInfo;
import com.hh.api.pojo.BidInfoProduct;
import com.hh.api.service.InvestService;
import com.hh.common.constants.ZFBConstant;
import com.hh.common.util.CommonUtil;
import com.hh.dataservice.mapper.BidInfoMapper;
import com.hh.dataservice.mapper.FinanceAccountMapper;
import com.hh.dataservice.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0-SNAPSHOT")
public class InvestServiceImpl implements InvestService {
    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Resource
    private ProductInfoMapper productInfoMapper;
    /*查询某个产品的投资记录*/
    @Override
    public List<BidInfoProduct> queryBidListByproductId(Integer productId, Integer pageNo, Integer pageSize) {
        List<BidInfoProduct> bidList = new ArrayList<>();
        if (productId != null && productId > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            bidList = bidInfoMapper.selectByProductId(productId, offset, pageSize);
        }
        return bidList;
    }

    /*投资理财产品，1投资成功*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer investProduct(Integer uid, Integer productId, BigDecimal money) {
        int result = 0; // 默认，参数不正确
        int rows = 0;
        // 参数检查验证
        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                &&(money != null && money.intValue() % 100 == 0 && money.intValue() >= 100)) {
            // 查询账户金额
            FinanceAccount account = financeAccountMapper.selectByUidForUpdate(uid);
            // 检查产品是否可以购买
            if (account != null) {
                if (CommonUtil.ge(account.getAvailableMoney(), money)) {
                    // 资金满足购买要求
                    ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
                    if (productInfo != null && productInfo.getProductStatus() == ZFBConstant.PRODUCT_STATUS_SELL) {
                        if (CommonUtil.ge(productInfo.getLeftProductMoney(), money)
                                && CommonUtil.ge(money, productInfo.getBidMinLimit())
                                && CommonUtil.ge(productInfo.getBidMaxLimit(), money)) {
                            // 执行到这里，可以进行购买了，扣除账号的资金
                            rows = financeAccountMapper.updataAvailableMoneyByInvest(uid, money);
                            if (rows < 1) {
                                throw new RuntimeException("投资更新账户资金失败");
                            }

                            // 扣除产品剩余可投资金额
                            rows = productInfoMapper.updateLeftProductMoney(productId, money);
                            if (rows < 1) {
                                throw new RuntimeException("投资更新产品剩余金额失败");
                            }

                            // 创建投资记录
                            BidInfo bidInfo = new BidInfo();
                            bidInfo.setBidMoney(money);
                            bidInfo.setBidStatus(ZFBConstant.INVEST_STATUS_SUCCESS);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setProdId(productId);
                            bidInfo.setUid(uid);
                            bidInfoMapper.insertSelective(bidInfo);

                            // 判断产品是否卖完
                            ProductInfo dbProductInfo = productInfoMapper.selectByPrimaryKey(productId);
                            if (dbProductInfo.getLeftProductMoney().compareTo(new BigDecimal(0)) == 0) {
                                rows = productInfoMapper.updateSelled(productId);
                                if (rows < 1) {
                                    throw new RuntimeException("投资更新产品满标失败");
                                }
                            }

                            // 最后投资成功
                            result = 1;
                        }
                    } else {
                        result = 4; // 理财产品不存在
                    }
                } else {
                    result = 3; // 资金不足
                }
            }
        } else {
            result = 2; // 资金账户不存在
        }
        return result;
    }
}
