package com.tensquare.task;

import com.tensquare.pipeline.ArticlePipeline;
import com.tensquare.pipeline.ArticleTextPipeline;
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

    @Autowired
    private ArticleTextPipeline articleTextPipeline;

    /**
     * 定时任务，用来指定爬虫的定时任务类，AI
     */
    @Scheduled(cron = "0 45 13 * * *")
    public void aiTask() {
        System.out.println("爬取AI文章");
        Spider spider = Spider.create(articleProcessor);
        spider.addUrl("https://www.csdn.net/nav/ai");
        //设置频道id
        articlePipeline.setChannelId("ai");
        spider.addPipeline(articlePipeline);
        articleTextPipeline.setChannelId("ai");
        spider.addPipeline(articleTextPipeline);
        spider.setScheduler(redisScheduler);

        spider.start();
    }


    /**
     * 爬取db文章
     */
    @Scheduled(cron = "0 34 13 * * *")
    public void dbTask() {
        System.out.println("爬取DB文章");
        Spider spider = Spider.create(articleProcessor);
        spider.addUrl("https://www.csdn.net/nav/db");
        //设置频道id
        articlePipeline.setChannelId("db");
        spider.addPipeline(articlePipeline);
        articleTextPipeline.setChannelId("db");
        spider.addPipeline(articleTextPipeline);
        spider.setScheduler(redisScheduler);

        spider.start();
    }

    /**
     * 爬取web文章
     */
    @Scheduled(cron = "0 37 13 * * *")
    public void webTask() {
        System.out.println("爬取WEB文章");
        Spider spider = Spider.create(articleProcessor);
        spider.addUrl("https://www.csdn.net/nav/web");
        //设置频道id
        articlePipeline.setChannelId("web");
        spider.addPipeline(articlePipeline);
        articleTextPipeline.setChannelId("web");
        spider.addPipeline(articleTextPipeline);
        spider.setScheduler(redisScheduler);

        spider.start();
    }

}
