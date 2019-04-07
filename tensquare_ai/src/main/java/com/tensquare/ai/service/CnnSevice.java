package com.tensquare.ai.service;

import com.tensquare.ai.util.CnnUtil;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.IKUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 卷积神经网络模型构建及训练
 */
@Service
public class CnnSevice {

    @Value("${ai.dataPath}")
    private String dataPath;//语料库的根目录
    @Value("${ai.vecModel}")
    private String vecModel;//词向量模型
    @Value("${ai.cnnModel}")
    private String cnnModel;//卷积神经网络模型

    /**
     * 构建卷积神经网络模型
     */
    public void build(){
        try {
            //1.创建计算图对象
            ComputationGraph computationGraph = CnnUtil.createComputationGraph(100);

            //2.加载训练数据集
            String[] childPath = {"ai","db","web"};
            DataSetIterator dataSet = CnnUtil.getDataSetIterator(dataPath, childPath, vecModel);

            //3.训练模型
            computationGraph.fit(dataSet);

            //删除之前的模型
            new File(cnnModel).delete();
            //4.保存模型
            ModelSerializer.writeModel(computationGraph,cnnModel,true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *  卷积神经网络模型格局分词的内容进行预言
     *  返回各分类的百分比
     * @param content
     * @return
     */
    public Map textClassify(String content){

        Map<String, Double> predictions = null;
        //1.语言
        try {
            content = IKUtil.split(content," ");

            String[] childPath = {"ai","db","web"};
            //2.获取预言的结果
            //String 表示 是哪个分类（ai、db、web） double 表示分类百分比的大小
            predictions = CnnUtil.predictions(vecModel, cnnModel, dataPath, childPath, content);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return predictions;
    }



}
