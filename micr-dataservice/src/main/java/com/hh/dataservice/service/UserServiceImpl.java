package com.hh.dataservice.service;

import com.hh.api.model.FinanceAccount;
import com.hh.api.model.User;
import com.hh.api.pojo.UserAccountInfo;
import com.hh.api.service.UserService;
import com.hh.common.util.CommonUtil;
import com.hh.dataservice.mapper.FinanceAccountMapper;
import com.hh.dataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class, version = "1.0-SNAPSHOT")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Value("${zfb.config.password-salt}")
    private String passwordSalt;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)) {
            user = userMapper.selectByPhone(phone);
        }
        return user;
    }

    /*用户注册*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized int userRegister(String phone, String pword) {
        int result = 0; // 默认参数不正确
        if (CommonUtil.checkPhone(phone)
                && pword != null
                && pword.length() == 32) {
            // 判断手机号在库中是否存在
            User queryUser = userMapper.selectByPhone(phone);
            if (queryUser == null) {

                // 注册密码的md5二次加密，给初始密码加盐（salt）
                String newPassword = DigestUtils.md5Hex(pword + passwordSalt);
                // 注册u_user
                User user = new User();
                user.setPhone(phone);
                user.setLoginPassword(newPassword);
                user.setAddTime(new Date());
                userMapper.insertReturnPrimaryKey(user);

                // 获取主键
                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(account);

                // 成功
                result = 1;
            } else {
                // 手机号已存在
                result = 2;
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String pword) {
        User user = null;
        if (CommonUtil.checkPhone(phone) && (pword != null && pword.length() == 32)) {
            String newPassword = DigestUtils.md5Hex(pword + passwordSalt);
            user = userMapper.selectLogin(phone, newPassword);

            // 更新最后的登陆时间
            if (user != null) {
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
        return user;
    }

    @Override
    public boolean modifyRealName(String phone, String name, String idCard) {
        int rows = 0;
        if (!StringUtils.isAnyBlank(phone, name, idCard)) {
            rows = userMapper.updateRealName(phone, name, idCard);
        }
        return rows > 0;
    }

    /*获取用户资金信息*/
    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0) {
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }

    /*查询用户*/
    @Override
    public User queryById(Integer uid) {
        User user = null;
        if (uid != null && uid > 0) {
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}
