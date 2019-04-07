package com.tensquare.pipeline;

import com.tensquare.dao.ArticleDao;
import com.tensquare.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.IdWorker;

@Component
public class ArticlePipeline implements Pipeline {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;


    private String channelId;
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    /**
     * 自定制的pipeline，用来处理爬取到的数据
     *
     * @param resultItems
     * @param task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String content = resultItems.get("content");

        Article article = new Article();
        article.setId(idWorker.nextId() + "");
        article.setTitle(title);
        article.setContent(content);
        article.setChannelid(channelId);

        //保存到数据库
        articleDao.save(article);
    }




}
