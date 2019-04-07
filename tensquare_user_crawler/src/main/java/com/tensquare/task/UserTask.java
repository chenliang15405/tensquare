package com.tensquare.task;

import com.tensquare.pipeline.UserPipeline;
import com.tensquare.processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@Component
public class UserTask {

    @Autowired
    private UserProcessor userProcessor;
    @Autowired
    private UserPipeline userPipeline;
    @Autowired
    private RedisScheduler redisScheduler;

    @Scheduled(cron = "0 10 12 * * *")
    public void userTask(){
        System.out.println("爬取用户数据");
        Spider spider = new Spider(userProcessor);
        spider.addUrl("https://blog.csdn.net");
        spider.addPipeline(userPipeline);
        spider.setScheduler(redisScheduler);
        spider.start();

    }

}
