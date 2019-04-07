package com.tensquare.ai.task;

import com.tensquare.ai.service.CnnSevice;
import com.tensquare.ai.service.Word2VecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *  合并分词数据，形成语料库任务
 */
@Component
public class TrainTask {

    @Autowired
    private Word2VecService word2VecService;

    @Autowired
    private CnnSevice cnnSevice;

    /**
     *  合并分词预料库
     *  构建词向量模型
     */
    @Scheduled(cron = "0 33 14 * * *")
    public void trainModel(){
        System.out.println("合并分词语料库开始");
        word2VecService.mergeword();
        System.out.println("合并分词语料库结束");

        System.out.println("构建词向量模型开始");
        word2VecService.build();
        System.out.println("构建词向量模型结束");

        System.out.println("构建卷积神经网络模型开始");
        cnnSevice.build();
        System.out.println("构建卷积神经网络模型结束");

    }


}
