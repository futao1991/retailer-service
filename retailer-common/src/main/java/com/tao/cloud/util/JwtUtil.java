package com.tao.cloud.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tao.cloud.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public static final String SECRET = "sdjhakdhajdklsl;o653632";

    /**
     * 用户登录成功后生成Jwt
     * @param expireTime  jwt过期时间
     * @param user        user
     */
    public static String createJWT(int expireTime, User user) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, expireTime);
        Date expireDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        return JWT.create()
                .withHeader(map)
                .withClaim("id", user.getId())
                .withClaim("username", user.getName())
                .withClaim("password", user.getPassword())
                .withSubject("accessToken")//
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 校验token
     * @param token     token
     * @return
     */
    public static Map<String, Claim> check(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        }catch (Exception e){
            throw new RuntimeException("凭证已过期，请重新登录");
        }
        return jwt.getClaims();
    }
}
