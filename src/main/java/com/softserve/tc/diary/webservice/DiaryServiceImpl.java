package com.softserve.tc.diary.webservice;

import java.sql.Timestamp;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.log.Log;

@WebService
public class DiaryServiceImpl implements DiaryService {
    private static Logger LOG = Log.init("DiaryServiceImpl");
    
    private UserDAO userDAO = new UserDAOImpl();
    private RecordDAO recordDAOImpl = new RecordDAOImpl();
    
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
                    String.format("Incorrect password of user with nickname %s",
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
    
    @Override
    public boolean addRecord(String nickname, Status status, String record) {
        User user = userDAO.findByNickName(nickname);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickname));
                    
            return false;
        } else {
            Timestamp createdTime =
                    new Timestamp(new java.util.Date().getTime());
                    
            Record newRecord = new Record(user.getUuid(), createdTime, record,
                    null, status);
            recordDAOImpl.create(newRecord);
            
            return true;
        }
    }
    
    @Override
    public boolean removeRecord(String nickname, String record) {
        // TODO Ask about Record. and nickname(maybe there must be session?)
        return false;
    }
    
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
