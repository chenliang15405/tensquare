package com.tensquare.ai.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Word2vec是一种比较流行的自然语言算法，能创建可以输入深度神经网络的神经词向
 * 量。
 */
@Service
public class Word2VecService {

    @Value("${ai.wordLib}")
    private String wordLib;
    @Value("${ai.dataPath}")
    private String dataPath;
    @Value("${ai.vecModel}")
    private String vecModelPath;

    /**
     * 合并爬取到的各个区域的分词之后的数据，产生语料库
     */
    public void mergeword(){
        List<String> files = FileUtil.getFiles(dataPath);

        try {
            FileUtil.merge(wordLib,files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建词向量模型 vecmodel
     * 根据分词语料库
     */
    public void build(){
        try {
            //加载分词语料库
            SentenceIterator iterator = new LineSentenceIterator(new File(wordLib));

            Word2Vec vec = new Word2Vec.Builder()
                    .minWordFrequency(5) //分词语料库中词语出现最少的次数
                    .iterations(1) //设置迭代次数
                    .layerSize(100) //词向量维度
                    .seed(42) //随机种子
                    .windowSize(5) //5
                    .iterate(iterator) //加载分词语料库
                    .build();

            vec.fit(); //构建词向量模型

            //删除原来的词向量模型
            new File(vecModelPath).delete();

            //输出词向量模型
            WordVectorSerializer.writeWordVectors(vec,vecModelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
