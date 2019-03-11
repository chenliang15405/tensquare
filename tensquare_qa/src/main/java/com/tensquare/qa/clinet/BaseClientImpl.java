package com.tensquare.qa.clinet;

import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class BaseClientImpl implements BaseClient {

    /**
     * 熔断器中的方法，如果base微服务挂掉，则调用这个熔断方法
     * @param labelId
     * @return
     */
    @Override
    public Result findById(String labelId) {
        System.out.println("base熔断器");
        return new Result(false,StatusCode.ERROR,"熔断器触发");
    }
}
