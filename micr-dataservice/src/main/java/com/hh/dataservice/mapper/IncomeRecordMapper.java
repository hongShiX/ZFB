package com.hh.dataservice.mapper;

import com.hh.api.model.IncomeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord row);

    int insertSelective(IncomeRecord row);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord row);

    int updateByPrimaryKey(IncomeRecord row);

    /*到期的收益记录*/
    List<IncomeRecord> selectExpiredIncome(@Param("expiredDate") Date expiredDate);
}