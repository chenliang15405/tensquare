package com.tensquare.pipeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.HTMLUtil;
import util.IKUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 *  分词pipeline
 */
@Component
public class ArticleTextPipeline implements Pipeline {

    @Value("${ai.dataPath}")
    private String dataPath;

    private String channelId;//频道id
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = HTMLUtil.delHTMLTag(resultItems.get("title"));
        String content = HTMLUtil.delHTMLTag(resultItems.get("content"));

        try {
            //分词
            String split = IKUtil.split(title + content, " ");
            PrintWriter printWriter = new PrintWriter(dataPath +channelId+ File.separator+ UUID.randomUUID() + ".txt");
            printWriter.write(split);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
