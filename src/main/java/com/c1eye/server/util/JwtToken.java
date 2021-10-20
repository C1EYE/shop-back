package com.c1eye.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author c1eye
 * time 2021/10/15 11:16
 */
@Component
public class JwtToken {

    private static String jwtKey;
    private static Integer expiredTimeIn;
    //用来做api权限的字段，数字越大权限越大
    private static Integer defaultScope = 8;


    @Value("${c1eye.security.jwt-key}")
    public void setJwtKey(String jwtKey) {
        JwtToken.jwtKey = jwtKey;
    }

    @Value("${c1eye.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn){
        JwtToken.expiredTimeIn = expiredTimeIn;
    }

    /**
     * 获取jwt令牌
     * @param uid
     * @param scope
     * @return
     */
    public static String makeToken(Long uid, Integer scope) {
        return getToken(uid, scope);
    }

    public static String makeToken(Long uid) {
        return getToken(uid, JwtToken.defaultScope);
    }

    /**
     * 获取令牌
     * @param uid
     * @param scope
     * @return
     */
    private static String getToken(Long uid, Integer scope) {
        Algorithm algorithm = Algorithm.HMAC256(jwtKey);
        Map<String, Date> map = JwtToken.calculateExpiredIssues();
        return JWT.create().
                withClaim("uid", uid).
                          withClaim("scope", scope).
                          withExpiresAt(map.get("expiredTime")).
                          withIssuedAt(map.get("now")).
                          sign(algorithm);
    }

    /**
     * 计算过期时间
     * @return
     */
    private static Map<String, Date> calculateExpiredIssues() {
        HashMap<String, Date> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, JwtToken.expiredTimeIn);
        map.put("now", now);
        map.put("expiredTime", calendar.getTime());
        return map;
    }

    /**
     * 令牌校验
     * @param token
     * @return
     */
    public static Optional<Map<String, Claim>> getClaims(String token){
        DecodedJWT decodedJWT;
        Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            decodedJWT = jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    //校验令牌
    public static Boolean verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtToken.jwtKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            //不合法会抛出异常
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            return false;
        }
        return true;
    }
}
