package com.tensquare.sms.listener;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")//指定queue的名称，这个名称暂时是在132.232.104.247:15672中创建的队列名称,username:guest password:guest
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;
    @Value("${aliyun.sms.sign_name}")
    private String signCode;

    /**
     * 处理指定queue中的消息
     */
    @RabbitHandler
    public void excuteSms(Map<String,String> map){
        String mobile = map.get("mobile");
        String checkcode = map.get("checkcode");

        //TODO 通过工具类发送短信

        //TODO 删除redis中数据（也可以不删除，省短信条数，等过期时间就可以）

    }

}
