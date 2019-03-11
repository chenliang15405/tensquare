package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserClient userClient;

    /**
     * 添加好友或者非好友
     * @return
     */
    @RequestMapping(value = "/like/{friendId}/{type}",method = RequestMethod.POST)
    public Result addFriend(@PathVariable("friendId") String friendId,@PathVariable("type") String type){
        //验证是否登录，并且拿到当前登录的用户的id
        Claims claims = (Claims) request.getAttribute("admin_user");
        if(claims==null){
            return new Result(true,StatusCode.ACCESSERROR,"权限不足！");
        }
        //从token得到用户id
        String userId = claims.getId();
        //判断是添加好友还是非好友
        if(type!=null){
            if(type.equals("1")){
                //添加好友
                int flag = friendService.addFriend(userId,friendId);
                if(flag == 0){
                    return new Result(true,StatusCode.ERROR,"不能重复添加好友！");
                }
                userClient.updateFansAndFollowCount(userId,friendId,1);
                return new Result(true,StatusCode.OK,"添加成功！");
            }else if(type.equals("2")){
                //添加非好友
                int flag = friendService.addNoFriend(userId,friendId);
                if(flag == 0){
                    return new Result(true,StatusCode.ERROR,"不能重复添加非好友！");
                }
                return new Result(true,StatusCode.OK,"添加成功！");
            }

            return new Result(true,StatusCode.ERROR,"参数异常！");
        }
        return new Result(true,StatusCode.ERROR,"参数异常！");
    }


    /**
     * 删除好友
     * @return
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        //验证是否登录，并且拿到当前登录的用户的id
        Claims claims = (Claims) request.getAttribute("admin_user");
        if(claims==null){
            return new Result(true,StatusCode.ACCESSERROR,"权限不足！");
        }
        //从token得到用户id
        String userid = claims.getId();
        friendService.deleteFriend(userid,friendid);
        //更新user表的fans和关注数
        userClient.updateFansAndFollowCount(userid,friendid,-1);
        return new Result(true,StatusCode.OK,"删除好友成功！");
    }

}
