package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private IdWorker idWorker;


    /**
     * 向索引库中增加文章
     * @param article
     */
    public void save(Article article) {
        article.setId(idWorker.nextId() + "");
        articleDao.save(article);
    }

    /**
     * 根据关键字 查询索引库中的数据，分页查询
     * @param key
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByKey(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Article> pageData = articleDao.findByTitleOrContent(key, key, pageable);
        return pageData;
    }
}
