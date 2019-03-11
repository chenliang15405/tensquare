package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    public NoFriend findByUseridAndFriendid(String userid, String friendid);

    @Modifying
    @Query(value = "update tb_friend set islike = ? where userid = ? and friendid = ?",nativeQuery = true)
    void updateIslike(String islike, String userid, String friendid);

}
