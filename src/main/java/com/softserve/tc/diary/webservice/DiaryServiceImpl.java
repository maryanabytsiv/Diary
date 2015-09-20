package com.softserve.tc.diary.webservice;

import java.util.UUID;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

@WebService
public class DiaryServiceImpl implements DiaryService {
    private static Logger LOG = Log.init("DiaryServiceImpl");
    private UserDAOImpl userDAO = new UserDAOImpl();
    
    public String logIn(String nickName, String password) {
        User user = userDAO.findByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
                    
            return null;
        } else {
            if (user.getPassword().equals(password)) {
                String session = UUID.randomUUID().toString();
                userDAO.updateSessionByNickName(nickName, session);
                
                return session;
            }
            LOG.debug(
                    String.format("Icorrect password of user with nickname %s",
                            nickName));
                            
            return null;
        }
    }
    
    public boolean logOut(String nickName) {
        User user = userDAO.findByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
                    
            return false;
        }
        userDAO.updateSessionByNickName(nickName, null);
        
        return true;
    }
    //
    // @Override
    // public boolean addRecord(String nickname, Status status, String record) {
    // // TODO Auto-generated method stub
    // return false;
    // }
    //
    // @Override
    // public boolean removeRecord(String nickname, String record) {
    // // TODO Auto-generated method stub
    // return false;
    // }
    //
    // @Override
    // public List<Record> getAllRecords(String nickName, Date date) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public List<Record> getAllRecords(String nickName, String hashTag) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public Statistics viewSiteStatistics(String nickNameOfAdmin) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    
}
