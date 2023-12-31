package com.hh.pay.controller;

import com.hh.api.model.User;
import com.hh.pay.service.KuaiQianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQianController {
    @Resource
    private KuaiQianService kuaiQianService;
    /*接收来自vue项目的支付充值请求*/
    @GetMapping("/rece/recharge")
    public String receFrontRechargeKQ(Integer uid, BigDecimal rechargeMoney, Model model) {
        // 默认是错误视图
        String view = "err";

        if (uid != null && uid > 0 && rechargeMoney != null && rechargeMoney.doubleValue() > 0) {
            // 提交支付请求给快钱的form页面(thymeleaf)
            // 检查uid是否有效（该用户是否存在）
            try {
                User user = kuaiQianService.queryUser(uid);
                if (user != null) {
                    // 创建快钱支付接口需要的请求参数
                    Map<String, String> data = kuaiQianService.generateFormData(uid, user.getPhone(), rechargeMoney);
                    model.addAllAttributes(data);

                    // 创建充值记录
                    kuaiQianService.addRecharge(uid, rechargeMoney, data.get("orderId"));

                    // 把订单号存放到redis中
                    kuaiQianService.addOrderIdToRedis(data.get("orderId"));


                    view = "kqForm";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    // 接收快钱给商家的支付结果,异步通知，快钱以get方式，发送请求给商家
    @GetMapping("/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request) {
        kuaiQianService.kqNotify(request);
        return "<result>1</result><redirectur>http://localhost:8080/</redirectur>";
    }
}
