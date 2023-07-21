package com.hh.front.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hh.common.enums.RCode;
import com.hh.common.util.JwtUtil;
import com.hh.front.view.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class TokenInterceptor implements HandlerInterceptor {


    private String secretKey = "";

    public TokenInterceptor() {
    }

    public TokenInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是OPTIONS请求，放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        boolean isSend = false;


        try {
            // 获取token的值进行验证
            String headerUid = request.getHeader("uid");
            String headerToken = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(headerToken)) {
                String jwt = headerToken.substring(7);
                // 读取jwt
                JwtUtil jwtUtil = new JwtUtil(secretKey);
                Claims claims = jwtUtil.readJwt(jwt);

                // 获取jwt中的数据, uid
                Integer jwtUid = claims.get("uid", Integer.class);

                if (headerUid.equals(String.valueOf(jwtUid))) {
                    // token和发起请求的用户是同一个，请求可以被处理
                    isSend = true;
                }
            }

        } catch (Exception e) {
            isSend = false;
            e.printStackTrace();
        }

        // token没有验证通过，需要给vue错误提示
        if (!isSend) {
            // 返回json数据给前端
            RespResult result = RespResult.fail();
            result.setRCode(RCode.TOKEN_INVALID);

            // 使用HttpServletResponse输出
            String resJson = JSONObject.toJSONString(result);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(resJson);
            out.flush();
        }

        return isSend;
    }
}
