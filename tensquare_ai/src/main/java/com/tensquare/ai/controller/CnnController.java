package com.tensquare.ai.controller;

import com.tensquare.ai.service.CnnSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cnn")
public class CnnController {

    @Autowired
    private CnnSevice cnnSevice;

    @RequestMapping(value = "/textClassify",method = RequestMethod.POST)
    public Map textClasify(@RequestBody Map<String,String> content){
        return cnnSevice.textClassify(content.get("content"));
    }


}
