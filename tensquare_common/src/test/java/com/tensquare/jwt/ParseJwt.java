package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwt {

    public static void main(String[] args) {
        Claims claims = Jwts.parser().setSigningKey("tangsong")
                            .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_mmI4iLCJpYXQiOjE1NTIxOTc4MDR9.IhMicIljv0YzPtwR4bmSsc6ji3LbTE-OyHm_pa1HvbA")
                            .getBody();


        System.out.println("用户id" + claims.getId() );
        System.out.println("用户名" + claims.getSubject());
        System.out.println("登录事件" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));

        System.out.println("过期时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
        System.out.println("自定义的键值对： " +claims.get("role"));
    }
}
