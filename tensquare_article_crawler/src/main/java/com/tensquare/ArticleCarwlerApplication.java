package com.tensquare;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import util.IdWorker;

@SpringBootApplication
@EnableScheduling
public class ArticleCarwlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleCarwlerApplication.class,args);
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    @Bean
    public RedisScheduler redisScheduler(){
        return new RedisScheduler(redisHost);
    }

}
