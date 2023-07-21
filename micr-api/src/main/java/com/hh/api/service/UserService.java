package com.hh.api.service;

import com.hh.api.model.User;
import com.hh.api.pojo.UserAccountInfo;

public interface UserService {
    /*根据手机号查询数据*/
    User queryByPhone(String phone);

    /**
     * 用户注册
     * @param phone
     * @param pword
     * @return
     */
    int userRegister(String phone, String pword);

    /**
     * 登录方法
     * @param phone
     * @param pword
     * @return
     */
    User userLogin(String phone, String pword);

    /**
     * 更新实名认证信息
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    boolean modifyRealName(String phone, String name, String idCard);

    /*获取用户和资金信息*/
    UserAccountInfo queryUserAllInfo(Integer uid);

    /*查询用户*/
    User queryById(Integer uid);
}
