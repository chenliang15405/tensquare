package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "tensquare_article",type = "article")
public class Article implements Serializable {

    //如果数据库中数据删除了，索引库是不会同步删除，所以，删除数据库中的数据，最好是改变状态就行，
    //索引库就会同步状态，根据状态查询数据，否则删除数据不能同步

    //TODO 在安装es的时候，需要通过restful创建索引库，java可以创建type,和document
    @Id
    private String id;


    //是否索引，就是看该域是否能被搜索
    //是否分词，就是表示搜索的时候整体匹配还是单词匹配
    //是否存储，就是是否在页面上显示
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;

    private String state;//审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
