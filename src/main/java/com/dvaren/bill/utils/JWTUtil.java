package com.dvaren.bill.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dvaren.bill.config.ApiException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * @Author: xiang
 * @Date: 2021/5/11 21:11
 * <p>
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）,默认：{"alg": "HS512","typ": "JWT"}
 * payload的格式 设置：（用户信息、创建时间、生成时间）
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 */

public class JWTUtil {

    // 用于JWT进行签名加密的秘钥
    private static final String SECRET = "TnaX";

    //jwt过期时间
    private static final int VALIDITY = 3;
    /**
     * @Param: 传入需要设置的payload信息
     * @return: 返回token
     */
    public static String generateToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();

        // 将map内的信息传入JWT的payload中
        map.forEach(builder::withClaim);

        // 设置JWT令牌的过期时间为60
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_WEEK, 1);
        builder.withExpiresAt(instance.getTime());

        // 设置签名并返回token
        return builder.sign(Algorithm.HMAC256(SECRET));
    }
    /**
     * 验证token
     * @Param: 传入token
     * @return:
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**解密token
     * @Param: 传入token
     * @return: 解密的token信息
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }
    /**解密token
     * @Param: 传入token
     * @return: 获取token中的uid
     */
    public static String getUid(String token) throws ApiException {
        if (TextUtil.isEmpty(token)){
            throw new ApiException(400,"当前未登录");
        }
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaims().get("id").asString();
    }

    /**
     * 获取token失效时间
     *
     * @param token
     * @return
     */
    public static Date getExpirationDateFromToken(String token) throws ApiException {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getExpiresAt();
        } catch (Exception e) {
            throw new ApiException(500,"token验证出错=>"+e);
        }
    }

    /**
     * 判断是否允许刷新token
     * @param token
     * @return
     * @throws ApiException
     */
    public static Boolean allowRefreshToken(String token) throws ApiException {
        Date expirationDate;
        try {
            expirationDate = getExpirationDateFromToken(token);
        }catch (Exception e){
            throw new ApiException(500,"token验证出错->"+e);
        }
        //如果
        if(calLastedTime(new Date(), expirationDate) >= 60){
            return true;
        }
        return false;
    }

    /**
     * 计算两个日期相差的分钟
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calLastedTime(Date startDate, Date endDate) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println(simpleFormat.format(startDate)+"->"+simpleFormat.format(endDate));
        long a = startDate.getTime();
        long b = endDate.getTime();
        int c = (int) ((a - b) / (1000 * 60));
        return c;
    }
}