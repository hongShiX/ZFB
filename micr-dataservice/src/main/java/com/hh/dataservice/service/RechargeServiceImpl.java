package com.hh.dataservice.service;

import com.hh.api.model.RechargeRecord;
import com.hh.api.service.RechargeService;
import com.hh.common.constants.ZFBConstant;
import com.hh.common.util.CommonUtil;
import com.hh.dataservice.mapper.FinanceAccountMapper;
import com.hh.dataservice.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class, version = "1.0-SNAPSHOT")
public class RechargeServiceImpl implements RechargeService {
    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = rechargeRecordMapper.selectByUid(uid, offset, pageSize);
        }
         return records;
    }

    @Override
    public int addRechargeRecord(RechargeRecord record) {
        return rechargeRecordMapper.insert(record);
    }

    /*处理后续充值*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int handlerKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0;
        int rows = 0;
        // 查询订单
        RechargeRecord record = rechargeRecordMapper.selectByRechargeNo(orderId);
        if (record != null) {
            if (record.getRechargeStatus() == ZFBConstant.RECHARGE_STATUS_PROCESSING) {
                // 判断金额是否一致
                String fen = record.getRechargeMoney().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
                if (fen.equals(payAmount)) {
                    // 金额一致
                    if("10".equals(payResult)) {
                        // 充值成功,更新用户账户资金
                        rows = financeAccountMapper.updateAvailableMoneyByRecharge(record.getUid(), record.getRechargeMoney());
                        if (rows < 1) {
                            throw new RuntimeException("充值更新资金账户失败");
                        }
                        // 更新充值记录的状态
                        rows = rechargeRecordMapper.updateStatus(record.getId(), ZFBConstant.RECHARGE_STATUS_SUCCESS);

                        if (rows < 1) {
                            throw new RuntimeException("更新充值状态记录失败");
                        }
                        result = 1; // 成功
                    } else {
                        // 充值失败
                        rows = rechargeRecordMapper.updateStatus(record.getId(), ZFBConstant.RECHARGE_STATUS_FAIL);

                        if (rows < 1) {
                            throw new RuntimeException("更新充值状态记录失败");
                        }
                        result = 2; // 充值结果失败
                    }
                } else {
                    result = 4; // 金额不一致
                }
            } else {
                result = 3; // 订单已经处理过了
            }
        }
        return result;
    }
}
