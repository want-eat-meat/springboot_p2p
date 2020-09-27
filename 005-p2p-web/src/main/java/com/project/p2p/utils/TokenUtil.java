package com.project.p2p.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.p2p.pojo.User;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    public static final String SECRET = "sdjhakdhajdklsl;o653632";

    /**
     * 生成Token
     * @param user
     * @param expireTime 过期时间 s
     * @return Token字符串
     * @throws Exception
     */
    public static String createToken(User user, Integer expireTime) throws Exception {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, expireTime);
        Date expireDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create()
                .withHeader(map)
                .withClaim("userId", user.getId())
                .withClaim("userPhone", user.getPhone())
                .withSubject("测试")
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 解析Token
     * @param token
     * @return
     */
    public static Map<String, Claim> parseToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }

    /**
     * 验证Token
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token)throws Exception{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt;
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public static User verifyUser(String token){
        User user = new User();
        try{
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            user.setId(jwt.getClaim("userId").asInt());
            user.setPhone(jwt.getClaim("userPhone").asString());
            return user;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}
