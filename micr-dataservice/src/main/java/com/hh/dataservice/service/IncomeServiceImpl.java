package com.hh.dataservice.service;

import com.hh.api.model.BidInfo;
import com.hh.api.model.IncomeRecord;
import com.hh.api.model.ProductInfo;
import com.hh.api.service.IncomeService;
import com.hh.common.constants.ZFBConstant;
import com.hh.dataservice.mapper.BidInfoMapper;
import com.hh.dataservice.mapper.FinanceAccountMapper;
import com.hh.dataservice.mapper.IncomeRecordMapper;
import com.hh.dataservice.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class, version = "1.0-SNAPSHOT")
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private IncomeRecordMapper incomeRecordMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;


    /*收益计划*/
    @Override
    @Transactional
    public synchronized void generateIncomePlan() {
        // 获取要处理的理财产品记录
        Date current = new Date();
        Date beginTime = DateUtils.truncate(DateUtils.addDays(current, -1), Calendar.DATE);
        Date endTime = DateUtils.truncate(current, Calendar.DATE);

        /*查询所有满标的产品*/
        List<ProductInfo> productInfos = productInfoMapper.selectFullTimeProduct(beginTime, endTime);

        // 查询每个理财产品的多个投资记录
        BigDecimal income = null; // 利息
        BigDecimal dayRate = null; // 日利率
        BigDecimal cycle = null; // 周期
        Date incomeDate = null; //到期时间

        int rows = 0;
        for (ProductInfo productInfo : productInfos) {
            // 日利率
            dayRate = productInfo.getRate().divide(new BigDecimal("365"), 10, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

            // 产品类型不同，周期不同
            if (productInfo.getProductType() == ZFBConstant.PRODUCT_TYPE_XINSHOUBAO) { // 以天为单位
                cycle = new BigDecimal(productInfo.getCycle());
                incomeDate = DateUtils.addDays(productInfo.getProductFullTime(), 1 + productInfo.getCycle());
            } else {
                cycle = new BigDecimal(productInfo.getCycle() * 30);
                incomeDate = DateUtils.addDays(productInfo.getProductFullTime(), 1 + productInfo.getCycle() * 30);
            }

            List<BidInfo> bidInfos = bidInfoMapper.selectByProId(productInfo.getId());
            // 计算每笔投资的利息和到期时间
            for (BidInfo bidInfo : bidInfos) {
                // 利息： 利息 = 本金 * 周期 * 利率
                income = bidInfo.getBidMoney().multiply(cycle).multiply(dayRate);

                // 创建收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeStatus(ZFBConstant.INCOME_STATUS_PLAN);
                incomeRecord.setProdId(productInfo.getId());
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setIncomeMoney(income);
                incomeRecordMapper.insertSelective(incomeRecord);
            }

            // 更新产品的状态
            rows = productInfoMapper.updateStatus(productInfo.getId(), ZFBConstant.PRODUCT_STATUS_PLAN);
            if (rows < 1) {
                // 更新失败
                throw new RuntimeException("生成收益计划，更新产品状态为2失败");
            }
        }
    }


    /*收益返还*/

    @Transactional
    @Override
    public synchronized void generateIncomeBack() {
        // 获取到期的收益记录
        Date curDate = new Date();
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(curDate, -1), Calendar.DATE);
        List<IncomeRecord> incomeRecords = incomeRecordMapper.selectExpiredIncome(expiredDate);

        int rows = 0;
        // 把每个收益进行返还：本金 + 利息
        for (IncomeRecord ir : incomeRecords) {
            rows = financeAccountMapper.updateAvailableMoneyByIncomeBack(ir.getUid(), ir.getBidMoney(), ir.getIncomeMoney());
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新账户资金失败");
            }

            // 更新收益记录的状态为1表示收益到账
            ir.setIncomeStatus(ZFBConstant.INCOME_STATUS_BACK);
            rows = incomeRecordMapper.updateByPrimaryKey(ir);
            if (rows < 1) {
                throw new RuntimeException("收益返还，更新收益记录的状态失败");
            }

        }
    }
}
