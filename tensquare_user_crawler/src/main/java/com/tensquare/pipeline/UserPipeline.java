package com.tensquare.pipeline;

import com.tensquare.dao.UserDao;
import com.tensquare.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import util.DownloadUtil;
import util.IdWorker;

import java.io.IOException;

@Component
public class UserPipeline implements Pipeline {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserDao userDao;

    @Value("${userImg.save.path}")
    private String imgSavePath;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取爬取的数据
        String nickname = resultItems.get("nickname");
        String img = resultItems.get("img");

        String fileName = img.substring(img.lastIndexOf("/")+1);

        //将数据保存到数据库中
        User user = new User();
        user.setId(idWorker.nextId() + "");
        user.setNickname(nickname);
        user.setAvatar(fileName);

        userDao.save(user);

        //将头像图片下载到本地
        try {
            DownloadUtil.download(img,fileName,imgSavePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
