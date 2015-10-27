package com.softserve.tc.diary.webservice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.connectionmanager.DataBase;
import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.dao.implementation.TagDAOImpl;
import com.softserve.tc.diary.dao.implementation.UserDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.PasswordHelper;
import com.softserve.tc.diary.util.UserFolderForPersonalData;

@WebService(endpointInterface = "com.softserve.tc.diary.webservice.DiaryService")
public class DiaryServiceImpl implements DiaryService {

	private static Logger LOG = Log.init("DiaryServiceImpl");
	
	private ConnectionManager connection = ConnectionManager.getInstance(DataBase.CLOUDDB);
	private UserDAOImpl userDAO = UserDAOImpl.getInstance(connection);
	private RecordDAOImpl recordDAO = RecordDAOImpl.getInstance(connection);
	private TagDAOImpl tagDAO = TagDAOImpl.getInstance(connection);
	private AddressDAOImpl addressDAO = AddressDAOImpl.getInstance(connection);

	public DiaryServiceImpl() {
		
	}

	@Override
	@WebMethod
	public String sayHello(String name) {

		return "Hello from WebService to " + name + "!";

	}

	@WebMethod
	public String logIn(String nickName, String password) {

		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));

			return null;
		} else {
			String encryptedPassword = "";
			try {
				encryptedPassword = password != null ? PasswordHelper.encrypt(password) : null;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			if (user.getPassword().equals(encryptedPassword)) {
				String session = UUID.randomUUID().toString();
				user.setSession(session);
				userDAO.update(user);

				return session;
			}
			LOG.debug(String.format("Incorrect password of user with nickname %s", nickName));

			return null;
		}
	}

	@WebMethod
	public boolean logOut(String nickName) {

		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));

			return false;
		}
		user.setSession(null);
		userDAO.update(user);

		return true;
	}

	@Override
	@WebMethod
	public Record addRecord(Record record, byte[] file) {

		Record rec = null;

		if (file == null) {
			String idRec = recordDAO.create(record);
			rec = recordDAO.readByKey(idRec);
			return record;
		} else {
			File serverFile = null;
			try {
				User user = userDAO.readByKey(record.getUserId());
				String url = UserFolderForPersonalData.getFolderForUser(user.getNickName());
				serverFile = new File(url + File.separator + record.getSupplement());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file);
				stream.close();
				record.setSupplement(record.getSupplement());
				String idRec = recordDAO.create(record);
				rec = recordDAO.readByKey(idRec);
				LOG.info("You successfully uploaded file=" + record.getSupplement());
			} catch (IOException e) {
				LOG.error("You failed to upload " + record.getSupplement() + " => " + e.getMessage());
			}

			return rec;
		}
	}

	@Override
	public boolean removeRecord(String nickname, String recordId) {

		User user = userDAO.readByNickName(nickname);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickname));
			return false;
		}
		Record record = recordDAO.readByKey(recordId);
		if (record == null) {
			LOG.debug(String.format("Record was not found by id %s", recordId));
			return false;
		}
		recordDAO.delete(record);
		return true;
	}

	@Override
	public List<Record> getAllRecordsByDate(String nickName, String date) {
		List<Record> records = new ArrayList<>();

		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));
			return null;
		}
		Timestamp dateOfRecord = Timestamp.valueOf(date);
		records = recordDAO.getRecordByNickNameAndDate(user.getUuid(), dateOfRecord);
		if (records.isEmpty()) {
			LOG.debug(String.format("Record was not found by date %s", date));
			return null;
		}

		return records;
	}

	@Override
	public List<Record> getAllRecordsByHashTag(String nickName, String hashTag) {
		List<Record> records = new ArrayList<>();

		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));
			return null;
		}
		Tag tag = tagDAO.getTagByMessage(hashTag);
		records = tagDAO.getListRecordsByTag(tag);
		if (records.isEmpty()) {
			LOG.debug(String.format("Record was not found by hashtag %s", hashTag));
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
			LOG.debug(String.format("User was not found by nickname %s", nickName));
			return null;
		}

		return user.getRole();
	}

	@Override
	@WebMethod
	public List<Record> getAllPublicRecords() {
		List<Record> list = recordDAO.getAllPublicRecords();
		Collections.sort(list, new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				return o2.getCreatedTime().getTime() > o1.getCreatedTime().getTime() ? 1 : -1;
			}
		});
		return list;
	}

	@Override
	@WebMethod
	public Record readByKey(String id) {
		Record rec = recordDAO.readByKey(id);
		return rec;
	}

	public int getUserAmountOfRecords(String nickName) {
		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));
			return 0;
		}
		int numOfRecords = recordDAO.getUserAmountOfRecord(user.getUuid());
		return numOfRecords;
	}

	@Override
	public void updateUserWithoutImage(User user) {
		LOG.info("Udate user");
		userDAO.update(user);
		LOG.info("Udate user successfull");
	}

	@Override
	public void updateUser(User user, byte[] file, String fileName) {
		LOG.info("Udate user");
		File serverFile = null;
		if (file != null) {
			try {
				// Creating the directory to store file
				String url = UserFolderForPersonalData.getFolderForUser(user.getNickName());
				serverFile = new File(url + File.separator + fileName);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file);
				stream.close();
				LOG.info("You successfully uploaded file=" + fileName);

			} catch (Exception e) {
				LOG.error("You failed to upload " + fileName + " => " + e.getMessage());
			}
		} else {
			LOG.error("You failed to upload " + fileName + " because the file was empty.");
		}
		user.setAvatar(fileName);
		userDAO.update(user);
		LOG.info("Udate user successfull");
	}

	@Override
	public void deleteUser(User user) {
		userDAO.delete(user);
	}

	public void createUser(User user) {
		userDAO.create(user);
	}

	public Tag getMostPopularTag() {
		Tag tag = tagDAO.getMostPopularTag();
		return tag;
	}

	public User getMostActiveUser() {
		User user = userDAO.getMostActiveUser();
		return user;
	}

	@Override
	public List<Record> getAllPublicRecordsByHashTag(String hashTag) {
		boolean existTag = tagDAO.checkIfTagExist(hashTag);
		if (existTag == false) {
			List<Record> list2 = new ArrayList<Record>();
			return list2;
		}
		Tag tag = tagDAO.getTagByMessage(hashTag);
		List<Record> list = tagDAO.getListRecordsByTag(tag);
		Iterator<Record> it = list.iterator();
		while (it.hasNext()) {
			Record r = it.next();
			if (r.getVisibility().equals(Status.PRIVATE)) {
				it.remove();
			}
		}
		return list;
	}

	public int[] getSexStatistic() {
		int[] sexStatistic = userDAO.getSexStatistic();
		return sexStatistic;
	}

    public String updateSession(String nickName, String session) {
      
      session = userDAO.updateSession(nickName,session);
      return session;
    }
	
	public void invalidateSession(String nickName, String session){
	       
	       userDAO.invalidateSession(nickName, session);
	}

	@Override
	@WebMethod
	public List<String> getDatesWithRecordsPerMonth(String nickName, String date) {
		
		User user = userDAO.readByNickName(nickName);
		LocalDateTime dateLocal = LocalDateTime.of(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)), 0, 0, 0);
		List<String> listOfDates = recordDAO.getDatesWichHaveRecordsPerMonth(user.getUuid(), dateLocal);
		return listOfDates;
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		List<Tag> list = tagDAO.getListTagsByPrefix(prefix);
		return list;
	}

	@Override
	public User getUserByKey(String userId) {
		User user = userDAO.readByKey(userId);
		return user;
	}

	@Override
	public List<String> getAllHashes() {
		List<Tag> listOfTags = tagDAO.getAll();
		List<String> listOfHashTags = new ArrayList<String>();
		for (Tag tag : listOfTags) {
			listOfHashTags.add(tag.getTagMessage());
		}

		return listOfHashTags;
	}

	@Override
	public Record updateRecord(Record record, byte[] file) {
		
		File serverFile = null;
		if (file == null) {
			String recordId = record.getUuid();
			recordDAO.update(record);
			record = recordDAO.readByKey(recordId);
			return record;
		} else {
			try {
				User user = userDAO.readByKey(record.getUserId());
				String url = UserFolderForPersonalData.getFolderForUser(user.getNickName());
				serverFile = new File(url + File.separator + record.getSupplement());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file);
				stream.close();
				record.setSupplement(record.getSupplement());
				recordDAO.update(record);
				LOG.info("You successfully uploaded file=" + record.getSupplement());
			} catch (IOException e) {
				LOG.error("You failed to upload " + record.getSupplement() + " => " + e.getMessage());
			}
			record = recordDAO.readByKey(record.getUuid());
			return record;
		}
	}

	@Override
	public List<User> getActiveUsers() {

		return userDAO.getActiveUsers();
	}

	@Override
	public User getUserByEmail(String email) {
		
		User user = null;
		user = userDAO.getUserByEmail(email);
		return user;
	}
	
    @Override
    @WebMethod
    public String[][] getRecDate() {
        
        return recordDAO.getRecordDate();
    }

	@Override
	public String getDataForGeoChactGraphic(String country) {
		List<Object> list = addressDAO.getDataForGeoChartGraphic(country);
		return new Gson().toJson(list);
	}

	@Override
	public void updateUserPassword(User user, String password)  {
		try {
			password = PasswordHelper.encrypt(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setPassword(password);
		userDAO.update(user);
	
	}

}
