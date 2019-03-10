package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    @RequestMapping(value = "/{spitId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId) {
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK, "查询成功", spit);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit) {
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId) {
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除成功");
    }


    /**
     * 根据父节点查询所有的子节点，分页查询
     * @param parentId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/comment/{parentId}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentId,@PathVariable int page,@PathVariable int size){
        Page<Spit> pageData = spitService.findByParentId(parentId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageData.getTotalElements(),pageData.getContent()));
    }


    /**
     * 点赞
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        //判断当前用户是否点赞，现在没有做认证，等待认证之后，可以取到userid
        //现在固定
        String userid = "111";

        if(redisTemplate.opsForValue().get("thumbup_" + userid)!=null){
            return new Result(false,StatusCode.REPERROR,"不能重复点赞");
        }
        spitService.thumbup(spitId);
        //点赞完成，将用户id设置为已点赞--就是将userid放入redis
        redisTemplate.opsForValue().set("thumbup_" + userid,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}
