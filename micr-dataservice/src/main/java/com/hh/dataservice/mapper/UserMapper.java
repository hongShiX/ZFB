package com.hh.dataservice.mapper;

import com.hh.api.model.User;
import com.hh.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /*统计注册的人数*/
    int selectCountUsers();

    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    /*使用手机号查询用户数据*/
    User selectByPhone(@Param("phone") String phone);

    /*添加记录 获取主键值*/
    int insertReturnPrimaryKey(User user);

    /*登录*/
    User selectLogin(@Param("phone") String phone, @Param("loginPassword") String password);

    /*更新实名认证信息*/
    int updateRealName(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

    /*查询用户账户信息*/
    UserAccountInfo selectUserAccountById(@Param("uid") Integer uid);
}