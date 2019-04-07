package com.tensquare.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 用户数据爬虫处理类
 */
@Component
public class UserProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        //设置爬取的url下面的子连接的规则
        page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a-z 0-9 -]+/article/details/[0-9]{8}").all());

        //爬取昵称和头像
        //通过指定text() 来获取标签中的文本内容
        String nickname = page.getHtml().xpath("//*[@id=\"uid\"]/text()").get();
        //拿到<img> 标签的父标签，<a> ,然后指定css属性，指定下面的img的src属性获取数据
        String img = page.getHtml().xpath("//*[@id=\"asideProfile\"]/div[1]/div[1]/a").css("img","src").toString();

        if(StringUtils.isNotEmpty(nickname) && StringUtils.isNotEmpty(img)){
            page.putField("nickname",nickname);
            page.putField("img",img);
        } else {
            page.setSkip(true);
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100);
    }
}
