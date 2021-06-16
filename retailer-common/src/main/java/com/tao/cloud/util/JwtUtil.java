package com.tao.cloud.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tao.cloud.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final Algorithm algorithm = Algorithm.HMAC256("mysecret");

    /**
     * 用户登录成功后生成Jwt
     * @param ttlMillis  jwt过期时间
     * @param user      user
     */
    public static String createJWT(long ttlMillis, User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis + ttlMillis);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create().withHeader(header)
                .withClaim("id", user.getId())
                .withClaim("username", user.getName())
                .withClaim("password", user.getPassword())
                .withExpiresAt(now)
                .sign(algorithm);
    }

    /**
     * 解密token       加密后的token
     * @param token    token
     * @param password 密码
     * @return
     */
    public static Claims parseJWT(String token, String password) {
        return Jwts.parser()
                .setSigningKey(password)
                .parseClaimsJws(token).getBody();
    }

    /**
     * 校验token
     * @param token     token
     * @return
     */
    public static Map<String, Claim> check(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaims();
        } catch (Exception e) {
            return null;
        }
    }
}
