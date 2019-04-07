package com.tensquare.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 文章爬虫类
 */
@Component
public class ArticleProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        //设置爬虫url中子连接的爬取的url规则
        page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a-z 0-9 -]+/article/details/[0-9]{8}").all());

        //将爬取的内容保存
        String title = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1").get();
        String content = page.getHtml().xpath("//*[@id=\"content_views\"]").get();
        if(StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(content)){
            page.putField("title",title);
            page.putField("content",content);
        } else {
            page.setSkip(true);//跳过
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100);
    }
}
