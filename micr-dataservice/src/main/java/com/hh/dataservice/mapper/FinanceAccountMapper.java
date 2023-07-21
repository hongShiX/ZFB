package com.hh.dataservice.mapper;

import com.hh.api.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount row);

    int insertSelective(FinanceAccount row);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount row);

    int updateByPrimaryKey(FinanceAccount row);

    /*给uid的记录上锁*/
    FinanceAccount selectByUidForUpdate(@Param("uid") Integer uid);

    /*更新资金*/
    int updataAvailableMoneyByInvest(@Param("uid") Integer uid, @Param("money") BigDecimal money);

    /*收益返还，更新资金*/
    int updateAvailableMoneyByIncomeBack(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney, @Param("incomeMoney") BigDecimal incomeMoney);

    /*充值更新金额*/
    int updateAvailableMoneyByRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);
}