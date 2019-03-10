package com.tensquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
    }

    //common中没有spring，管理对象的bean，所以需要将该类注册到spring中管理
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
