package com.hh;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTest {
    // 创建jwt
    // fcca60c8adf24998b8faf2788e170118
    @Test
    public void testCreateJwt() {
        String key = "fcca60c8adf24998b8faf2788e170118";

        // 创建SecretKey对象
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        Date curDate = new Date();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1000);
        data.put("name", "李四");
        data.put("role", "经理");
        // 创建jwt，使用Jwts.builder
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(curDate, 10)) // 十分钟之后过期
                .setIssuedAt(curDate)
                .setId(UUID.randomUUID().toString())
                .addClaims(data).compact();

        System.out.println("jwt== " + jwt);
    }

    @Test
    public void testReadJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2ODk1ODMwMjcsImlhdCI6MTY4OTU4MjQyNywianRpIjoiZjI5NThjODYtMjU0Ny00ZTI5LWEzOWItZTJmMWJkZTI3NmRjIiwicm9sZSI6Iue7j-eQhiIsIm5hbWUiOiLmnY7lm5siLCJ1c2VySWQiOjEwMDB9.kRMykEh52Mtb4sdbxYYAZV1XKaRi9jiuj6kP1uaY25k";
        String key = "fcca60c8adf24998b8faf2788e170118";

        // 创建SecretKey对象
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        // 解析jwt
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(jwt);

        // 读数据
        Claims body = claims.getBody();
        Integer userId = body.get("userId", Integer.class);
        System.out.println("userId = " + userId);
        String name = body.get("name", String.class);
        if (name != null) {
            System.out.println("name = " + name);
        }

        String jwtId = body.getId();
        System.out.println("jwtId = " + jwtId);

        Date expiration = body.getExpiration();
        System.out.println("过期时间" + expiration);
    }
}
