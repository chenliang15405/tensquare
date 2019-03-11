package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;


    /**
     * 添加好友
     * @param userId
     * @param friendId
     * @return
     */
    public int addFriend(String userId, String friendId) {
        //先判断userId -> friendId 是否有数据，如果有数据则返回0,
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendId);
        if(friend != null){
            return 0;
        }
        //直接添加好友，让userId -> friendId 方向的type改为0
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendId);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断friendId -》 userId是否有数据，如果有数据，则两个是双向好友，两个的type都改为1
        Friend friend1 = friendDao.findByUseridAndFriendid(friendId, userId);
        if(friend1 != null){
            //双向都更新为1
            friendDao.updateIslike("1",userId,friendId);
            friendDao.updateIslike("1",friendId,userId);
        }
        return 1;
    }

    /**
     * 添加非好友
     * @param userId
     * @param friendId
     * @return
     */
    public int addNoFriend(String userId, String friendId) {
        //先判断，是否是非好友，如果是非好友，直接返回0
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userId, friendId);
        if(noFriend != null){
            return 0;
        }
        //直接添加非好友，
        noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendId);
        noFriendDao.save(noFriend);
        return 1;
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid 到friendid这条数据
        friendDao.deleteFriend(userid,friendid);
        //更新friendid到userid的type为0
        friendDao.updateIslike("0",friendid,userid);
        //非好友表中添加userid 到friend的数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
