package com.tensquare.task;

import com.tensquare.pipeline.ArticlePipeline;
import com.tensquare.processor.ArticleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@Component
public class ArticleTask {

    @Autowired
    private ArticleProcessor articleProcessor;
    @Autowired
    private ArticlePipeline articlePipeline;
    @Autowired
    private RedisScheduler redisScheduler;


    /**
     * 定时任务，用来指定爬虫的定时任务类，AI
     */
    @Scheduled(cron = "0 16 11 * * *")
    public void aiTask() {
        System.out.println("爬取AI文章");
        Spider spider = Spider.create(articleProcessor);
        spider.addUrl("https://www.csdn.net/nav/ai");
        //设置频道id
        articlePipeline.setChannelId("ai");
        spider.addPipeline(articlePipeline);
        spider.setScheduler(redisScheduler);

        spider.start();

    }


}
