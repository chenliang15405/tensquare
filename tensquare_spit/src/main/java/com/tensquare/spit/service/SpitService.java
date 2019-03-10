package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Spit> findAll() {
        return spitDao.findAll();
    }


    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }


    public void save(Spit spit) {
        spit.set_id(idWorker.nextId() + "");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setComment(0);//评论数
        spit.setState("1");//状态
        //判断是否有父节点，如果有，则父节点的回复数+1
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            //更新父节点
            mongoTemplate.updateFirst(query, update, "spit");
        }
        //保存发布的新吐槽
        spitDao.save(spit);
    }

    public void update(Spit spit) {
        spitDao.save(spit);
    }

    public void deleteById(String id) {
        spitDao.deleteById(id);
    }


    /**
     * 根据父节点，查询子节点，分页查询
     *
     * @param parentId
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentId(String parentId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Spit> pageData = spitDao.findByParentid(parentId, pageable);
        return pageData;
    }

    /**
     * 点赞
     *
     * @param spitId
     */
    public void thumbup(String spitId) {
        //方式一： 效率问题
//        Spit spit = spitDao.findById(spitId).get();
//        if (spit != null) {
//            spit.setThumbup((spit.getThumbup() == null ? 0 : spit.getThumbup()) + 1);
//            spitDao.save(spit);
//        }

//        方式二：使用原生mongo命令，进行自增   db.spit.update({"_id","1"},{$inc:{thumbup:NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
