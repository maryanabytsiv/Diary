package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.Follower;
import com.softserve.tc.diary.entity.User;

public interface FollowerDAO extends BaseDAO<Follower> {
    
    List<User> getAllUserFollowers(String userUuid);
            
    List<User> getAllFollowedUsers(String followerUuid);
    
    void markUserWithNewRecord(String userUuid);
    
    void markAsViwed(String followerUuid);
    
    List<User> getAllReviewedUsers (String followerUuid);
    
    List<User> getAllNotReviewedUsers (String followerUuid);
}
