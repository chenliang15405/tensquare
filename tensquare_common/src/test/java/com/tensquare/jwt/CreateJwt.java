package com.tensquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwt {
    /**
     *
     * @param args
     */

    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                    .setId("666")
                    .setSubject("小明")
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256,"tangsong")//设置签名，参数指定加密算法和盐
                    .setExpiration(new Date(new Date().getTime()+60000))//设置过期时间
                    .claim("role","admin");//可以设置自定义的键值对
        System.out.println(jwtBuilder.compact());
    }

}
