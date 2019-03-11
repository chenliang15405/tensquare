package com.tensquare.qa.clinet;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 使用Feign实现服务之间的调用
 */
@FeignClient(value = "tensquare-base",fallback = BaseClientImpl.class)
public interface BaseClient {

    //feign调用eureka服务，必须制定@PathVariable("labelId") 中的名称
    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);

}
