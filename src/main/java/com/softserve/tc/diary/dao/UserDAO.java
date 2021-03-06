package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.User;

public interface UserDAO extends BaseDAO<User> {
    
    int countAllBySex(String sex);
    
    List<User> getByYearOfBirth(String yearOfBirth);
    
    User readByNickName(String nickName);
    
    List<User> getActiveUsers();
    
    void invalidateSession(String nickName, String session);
    
}
