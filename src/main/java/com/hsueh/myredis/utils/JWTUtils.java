package com.hsueh.myredis.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    private String secretkey = "12223";

    private int time = 10;

    /**
     *   token加密
     *
     *
     * */
    final public String generateToken(Long id) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, time);
//        DateTime newTime = now.offsetNew(DateField.MILLISECOND, 5000);
        Map<String, Object> payload = new HashMap<String, Object>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        payload.put("id", id);
        String token = JWTUtil.createToken(payload, secretkey.getBytes());
        return token;

    }
    /**
     *   验证是否可以解密 且是否过期
     *
     *
     * */

    final public Boolean verifyToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        jwt.setKey(secretkey.getBytes());
        boolean verifyKey = jwt.verify();
//        System.out.println(verifyKey);
//        Thread.sleep(6000);
        boolean verifyTime = jwt.validate(0);
//        System.out.println(verifyTime);
        return (verifyKey && verifyTime);
    }
    /**
     *  验证是否可以解密且是否过期 将唯一值返回
     *
     *
     * */
    final public Long parseToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        jwt.setKey(secretkey.getBytes());
        boolean verifyKey = jwt.verify();
        boolean verifyTime = jwt.validate(0);
        if (verifyKey && verifyTime) {
            JWTPayload pay = jwt.getPayload();
            Long id = (Long) pay.getClaim("id");
            return id;
        } else {
            return null;
        }
    }
}
