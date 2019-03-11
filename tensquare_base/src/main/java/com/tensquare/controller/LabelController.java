package com.tensquare.controller;

import com.tensquare.pojo.Label;
import com.tensquare.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/label")
@RequestScope //这个注解可以使用spring-cloud-bus中个更新配置文件中自定义的属性，也可以直接更新内容，否则bus只能更新框架中的属性
public class LabelController {

    @Autowired
    private LabelService labelService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Label> list = labelService.findAll();
        return new Result(true, StatusCode.OK, "查询成功",list);
    }

    @RequestMapping(value = "/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId) {
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK, "查询成功",label);
    }

    @RequestMapping(value = "/toplist", method = RequestMethod.GET)
    public Result findTopList() {
        return new Result(true, StatusCode.OK, "查询成功");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result findUserLabel() {
        return new Result(true, StatusCode.OK, "查询成功");
    }

    @RequestMapping(value="/{labelId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(value="/{labelId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String labelId){
        labelService.delete(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result search(@RequestBody Label label){
        List<Label> list = labelService.search(label);
        return new Result(true, StatusCode.OK, "查询成功",list);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@PathVariable int page,@PathVariable int size,@RequestBody Label label){
        Page<Label> pageData = labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }



}
