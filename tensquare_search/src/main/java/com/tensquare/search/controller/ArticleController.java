package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    /**
     * 索引库增加文章索引
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article) {
        articleService.save(article);
        return new Result(true, StatusCode.OK, "操作成功");
    }

    /**
     * 根据关键字，查询索引库，分页查询
     * @param key
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/{key}/{page}/{size}",method = RequestMethod.GET)
    public Result findByKey(@PathVariable String key, @PathVariable int page, @PathVariable int size){
        Page<Article> pageData = articleService.findByKey(key,page,size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Article>(pageData.getTotalElements(),pageData.getContent()));
    }

}
