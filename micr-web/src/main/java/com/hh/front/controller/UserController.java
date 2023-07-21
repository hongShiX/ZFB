package com.hh.front.controller;

import com.hh.api.model.User;
import com.hh.api.pojo.UserAccountInfo;
import com.hh.common.enums.RCode;
import com.hh.common.util.CommonUtil;
import com.hh.common.util.JwtUtil;
import com.hh.front.service.SmsService;
import com.hh.front.service.impl.RealNameServiceImpl;
import com.hh.front.vo.LoginInfo;
import com.hh.front.view.RespResult;
import com.hh.front.vo.RealNameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户的功能")
@RestController
@RequestMapping("/v1/user")
public class UserController extends BaseController{
    @Resource(name = "SmsCodeRegisterImpl")
    private SmsService RegisterSmsService;

    @Resource(name = "SmsCodeLoginImpl")
    private SmsService loginSmsService;

    @Resource
    private RealNameServiceImpl realNameServiceImpl;

    @Resource
    private JwtUtil jwtUtil;

    /**手机号注册用户*/
    @ApiOperation(value = "手机号注册用户")
    @PostMapping("/register")
    public RespResult userRegister(@RequestParam("phone") String phone,
                                                      @RequestParam("pword") String pword,
                                                      @RequestParam("scode") String scode) {
        RespResult result = RespResult.fail();
        // 检查参数
        if (CommonUtil.checkPhone(phone)) {
            if (pword != null && pword.length() == 32) {
                // 检查短信验证码
                if (RegisterSmsService.checkSmsCode(phone, scode)) {
                    // 可以注册
                    int registerResult = userService.userRegister(phone, pword);
                    if (registerResult == 1) {
                        result = RespResult.ok();
                    } else if (registerResult == 2) {
                        result.setRCode(RCode.PHONE_EXISTS);
                    } else if (registerResult == 0) {
                        result.setRCode(RCode.REQUEST_PARAM_ERR);
                    }
                } else {
                    // 短信验证码无效
                    result.setRCode(RCode.SMS_CODE_INVALID);
                }
            } else {
                result.setRCode(RCode.REQUEST_PARAM_ERR);
            }
        } else {
            // 手机号格式不正确
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }
    /**
     * 手机号是否存在
     */
    @ApiOperation(value = "手机号是否注册过", notes = "在注册过程中，判断手机号是否可以注册")
    @ApiImplicitParam(name = "phone", value = "手机号")
    @GetMapping ("/phone/exists")
    public RespResult phoneExists(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        result.setRCode(RCode.PHONE_EXISTS);
        // 1. 检查请求参数是否符合要求
        if (CommonUtil.checkPhone(phone)) {
            // 查询数据库
            User user = userService.queryByPhone(phone);
            if (user == null) {
                // 可以注册
                result = RespResult.ok();
            }
            // 把查询到的手机号放入到redis，然后检查手机号是否存在，可以查询redis
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

    @ApiOperation(value = "用户登录，获取访问token")
    @PostMapping("/login")
    /*登录，获取token-jwt*/
/*    public RespResult userLogin(@RequestParam("phone") String phone,
                                @RequestParam("pword") String pword,
                                @RequestParam("scode") String scode) throws Exception{*/

        public RespResult userLogin (@RequestBody LoginInfo loginInfo) throws Exception{
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(loginInfo.getPhone()) && (loginInfo.getPword() != null && loginInfo.getPword().length() == 32)) {
            if (loginSmsService.checkSmsCode(loginInfo.getPhone(), loginInfo.getScode())) {
                User user = userService.userLogin(loginInfo.getPhone(), loginInfo.getPword());
                if (user != null) {
                    // 登录成功，生成token
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", user.getId());
                    String jwtToken = jwtUtil.createJwt(data, 120);

                    result = RespResult.ok();
                    result.setAccessToken(jwtToken);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("uid", user.getId());
                    userInfo.put("phone", user.getPhone());
                    userInfo.put("name", user.getName());
                    result.setData(userInfo);
                } else {
                    result.setRCode(RCode.PHONE_LOGIN_PASSWORD_INVALID);
                }
            }
        } else {
            result.setRCode(RCode.PHONE_FORMAT_ERR);
        }
        return result;
    }

    /**
     * 实名认证
     */
    @ApiOperation(value = "实名认证", notes = "验证手机号、姓名、身份证号认证，是否一致")
    @PostMapping("/realname")
    public RespResult userRealName(@RequestBody RealNameVO realNameVO) {
        RespResult result = RespResult.fail();
        result.setRCode(RCode.REQUEST_PARAM_ERR);
        // 验证请求参数
        if (CommonUtil.checkPhone(realNameVO.getPhone())) {
            if (StringUtils.isNoneBlank(realNameVO.getName())
                    && StringUtils.isNoneBlank(realNameVO.getIdCard())) {
                // 判断用户是否已经做过
                User user = userService.queryByPhone(realNameVO.getPhone());
                if (user != null) {
                    if (StringUtils.isNotBlank(user.getName())) {
                        result.setRCode(RCode.IDENTIFY_REPEAT);
                    } else {
                        // 调用第三方接口，判断认证结果
                        boolean isReal = realNameServiceImpl.handleRealName(realNameVO.getPhone(), realNameVO.getName(), realNameVO.getIdCard());
                        if (isReal) {
                            result = RespResult.ok();
                        } else {
                            result.setRCode(RCode.IDENTIFY_FAIL);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 用户中心
     */
    @ApiOperation(value = "用户中心")
    @GetMapping("/usercenter")
    public RespResult userCenter(@RequestHeader(value = "uid", required = false) Integer uid) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            UserAccountInfo userAccountInfo = userService.queryUserAllInfo(uid);
            if (userAccountInfo != null) {
                result = RespResult.ok();
                Map<String, Object> data = new HashMap<>();
                data.put("name", userAccountInfo.getName());
                data.put("phone", userAccountInfo.getPhone());
                data.put("headerUrl", userAccountInfo.getHeaderImage());
                data.put("money", userAccountInfo.getAvailableMoney());
                if (userAccountInfo.getLastLoginTime() != null) {
                    data.put("loginTime", DateFormatUtils.format(userAccountInfo.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    data.put("loginTime", "-");
                }
                result.setData(data);
            }
        }
        return result;
    }
}
