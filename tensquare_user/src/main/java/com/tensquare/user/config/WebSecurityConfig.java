package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests 表示所有security全注解配置实现的开端，表示开始说明需要配置的权限
        //需要的权限分两部分，第一部分是拦截的路径，第二部分是访问该路径需要的权限
        //anyMatchers()表示拦截匹配的路径，permitAll表示任何权限都可以访问，直接放行所有
        //anyRequest表示任何请求，authenticated表示认证后才可以访问
        //.and().csrf().disable(); 表示拦截csrf攻击

        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
