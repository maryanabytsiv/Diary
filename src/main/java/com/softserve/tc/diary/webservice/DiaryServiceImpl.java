package com.softserve.tc.diary.webservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.connectionmanager.DBConnectionManager;
import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.dao.util.PasswordHelper;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;

@WebService(
        endpointInterface = "com.softserve.tc.diary.webservice.DiaryService")
public class DiaryServiceImpl implements DiaryService {
    
    private static Logger LOG = Log.init("DiaryServiceImpl");
    private static ConnectionManager conn = DBConnectionManager.getInstance();
    
    private UserDAO userDAO = new UserDAOImpl(conn);
    private RecordDAO recordDAOImpl = new RecordDAOImpl(conn);
    private TagDAOImpl tagDAOImpl = new TagDAOImpl(conn);
    
    @Override
    @WebMethod
    public String sayHello(String name) {
        
        return "Hello from WebService to " + name + "!";
        
    }
    
    @WebMethod
    public String logIn(String nickName, String password) {
        
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
                    
            return null;
        } else {
            String encryptedPassword =
                    password != null ? PasswordHelper.encrypt(password) : null;
                    
            if (user.getPassword().equals(encryptedPassword)) {
                String session = UUID.randomUUID().toString();
                user.setSession(session);
                userDAO.update(user);
                
                return session;
            }
            LOG.debug(
                    String.format("Incorrect password of user with nickname %s",
                            nickName));
                            
            return null;
        }
    }
    
    @WebMethod
    public boolean logOut(String nickName) {
        
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
                    
            return false;
        }
        user.setSession(null);
        userDAO.update(user);
        
        return true;
    }
    
    @Override
    @WebMethod
    public boolean addRecord(String nickname, Status status, String record) {
        
        User user = userDAO.readByNickName(nickname);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickname));
                    
            return false;
        } else {
            Timestamp createdTime =
                    new Timestamp(new java.util.Date().getTime());
                    
            Record newRecord =
                    new Record(user.getUuid(), createdTime, null, record,
                            null, status);
            recordDAOImpl.create(newRecord);
            
            return true;
        }
    }
    
    @Override
    public boolean removeRecord(String nickname, String recordId) {
        
        User user = userDAO.readByNickName(nickname);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickname));
            return false;
        }
        Record record = recordDAOImpl.readByKey(recordId);
        if (record == null) {
            LOG.debug(String.format("Record was not found by id %s",
                    recordId));
            return false;
        }
        recordDAOImpl.delete(record);
        return true;
    }
    
    @Override
    public List<Record> getAllRecordsByDate(String nickName, String date) {
        // TODO Auto-generated method stub
        List<Record> records = new ArrayList<>();
        
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
            return null;
        }
        Timestamp dateOfRecord = Timestamp.valueOf(date);
        records = recordDAOImpl.getRecordByNickNameAndDate(user.getUuid(),
                dateOfRecord);
        if (records.isEmpty()) {
            LOG.debug(String.format("Record was not found by date %s",
                    date));
            return null;
        }
        
        return records;
    }
    
    @Override
    public List<Record> getAllRecordsByHashTag(String nickName,
            String hashTag) {
        // TODO Auto-generated method stub
        List<Record> records = new ArrayList<>();
        
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
            return null;
        }
        Tag tag = tagDAOImpl.getTagByMessage(hashTag);
        records = tagDAOImpl.getListRecordsByTag(tag);
        if (records.isEmpty()) {
            LOG.debug(String.format("Record was not found by hashtag %s",
                    hashTag));
            return null;
        }
        
        return records;
    }

	@Override
	@WebMethod
	public User getUserByNickName(String nickName) {
		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));

			return null;
		} else {
			return user;
		}
	}

	@Override
	@WebMethod
	public List<User> getAllUsers() {
		List<User> usersList = userDAO.getAll();
		if (usersList == null) {
			LOG.debug(String.format("Table is empty"));
			return null;
		} else {
			return usersList;
		}
	}
    
    @Override
    public String getRoleByNickName(String nickName) {
        
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
            return null;
        }
        
        return user.getRole();
    }
    
    @Override
    @WebMethod
    public List<Record> getAllPublicRecords() {
    	RecordDAOImpl dao = new RecordDAOImpl();
    	List<Record> list = dao.getAllPublicRecords();
    	return list;	
    }

	@Override
	@WebMethod
	public Record readByKey(String id) {
		RecordDAOImpl dao = new RecordDAOImpl();
		Record rec = dao.readByKey(id);
		return rec;
	}
    
    public int getUserAmountOfRecords (String nickName) {
        User user = userDAO.readByNickName(nickName);
        if (user == null) {
            LOG.debug(String.format("User was not found by nickname %s",
                    nickName));
            return 0;
        }
        int numOfRecords = recordDAOImpl.getUserAmountOfRecord(user.getUuid());
        return numOfRecords;
    }

	@Override
	public void updateUser(User user) {
		userDAO.update(user);
		
	}

	@Override
	public void deleteUser(User user) {
		userDAO.delete(user);
		
	}
    
    // @Override
    // public Statistics viewSiteStatistics(String nickNameOfAdmin) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    
}
