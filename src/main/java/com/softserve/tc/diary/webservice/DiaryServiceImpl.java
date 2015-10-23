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
import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.dao.RecordDAO;
import com.softserve.tc.diary.dao.UserDAO;
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

	private UserDAO userDAO = new UserDAOImpl();
	private RecordDAO recordDAOImpl = new RecordDAOImpl();
	private TagDAOImpl tagDAOImpl = new TagDAOImpl();

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
		RecordDAOImpl daoRec = new RecordDAOImpl();
		Record rec = null;
		UserDAOImpl daoUser = new UserDAOImpl();
		if (file == null) {
			String idRec = daoRec.create(record);
			rec = daoRec.readByKey(idRec);
			return record;
		} else {
			File serverFile = null;
			try {
				User user = daoUser.readByKey(record.getUserId());
				String url = UserFolderForPersonalData.getFolderForUser(user.getNickName());
				serverFile = new File(url + File.separator + record.getSupplement());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file);
				stream.close();
				record.setSupplement(record.getSupplement());
				String idRec = daoRec.create(record);
				rec = daoRec.readByKey(idRec);
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
		Record record = recordDAOImpl.readByKey(recordId);
		if (record == null) {
			LOG.debug(String.format("Record was not found by id %s", recordId));
			return false;
		}
		recordDAOImpl.delete(record);
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
		records = recordDAOImpl.getRecordByNickNameAndDate(user.getUuid(), dateOfRecord);
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
		Tag tag = tagDAOImpl.getTagByMessage(hashTag);
		records = tagDAOImpl.getListRecordsByTag(tag);
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
		RecordDAOImpl dao = new RecordDAOImpl();
		List<Record> list = dao.getAllPublicRecords();
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
		RecordDAOImpl dao = new RecordDAOImpl();
		Record rec = dao.readByKey(id);
		return rec;
	}

	public int getUserAmountOfRecords(String nickName) {
		User user = userDAO.readByNickName(nickName);
		if (user == null) {
			LOG.debug(String.format("User was not found by nickname %s", nickName));
			return 0;
		}
		int numOfRecords = recordDAOImpl.getUserAmountOfRecord(user.getUuid());
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
		TagDAOImpl tagDAOImpl = new TagDAOImpl();
		Tag tag = tagDAOImpl.getMostPopularTag();
		return tag;
	}

	public User getMostActiveUser() {
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		User user = userDAOImpl.getMostActiveUser();
		return user;
	}

	@Override
	public List<Record> getAllPublicRecordsByHashTag(String hashTag) {
		TagDAOImpl dao = new TagDAOImpl();
		boolean existTag = dao.checkIfTagExist(hashTag);
		if (existTag == false) {
			List<Record> list2 = new ArrayList<Record>();
			return list2;
		}
		Tag tag = dao.getTagByMessage(hashTag);
		List<Record> list = dao.getListRecordsByTag(tag);
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
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		int[] sexStatistic = userDAOImpl.getSexStatistic();
		return sexStatistic;
	}

	public String updateSession(String nickName) {
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		String session = userDAOImpl.updateSession(nickName);
		return session;
	}
	
	public void invalidateSession(String nickName, String session){
	       UserDAOImpl userDAOImpl = new UserDAOImpl();
	       userDAOImpl.invalidateSession(nickName, session);
	}

	@Override
	@WebMethod
	public List<String> getDatesWithRecordsPerMonth(String nickName, String date) {
		RecordDAOImpl recordDAOImpl = new RecordDAOImpl();
		User user = userDAO.readByNickName(nickName);
		LocalDateTime dateLocal = LocalDateTime.of(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date.substring(8, 10)), 0, 0, 0);
		List<String> listOfDates = recordDAOImpl.getDatesWichHaveRecordsPerMonth(user.getUuid(), dateLocal);
		return listOfDates;
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		TagDAOImpl dao = new TagDAOImpl();
		List<Tag> list = dao.getListTagsByPrefix(prefix);
		return list;
	}

	@Override
	public User getUserByKey(String userId) {
		UserDAOImpl dao = new UserDAOImpl();
		User user = dao.readByKey(userId);
		return user;
	}

	@Override
	public List<String> getAllHashes() {
		TagDAOImpl tagDAOImpl = new TagDAOImpl();
		List<Tag> listOfTags = tagDAOImpl.getAll();
		List<String> listOfHashTags = new ArrayList<String>();
		for (Tag tag : listOfTags) {
			listOfHashTags.add(tag.getTagMessage());
		}

		return listOfHashTags;
	}

	@Override
	public Record updateRecord(Record record, byte[] file) {
		RecordDAOImpl daoRec = new RecordDAOImpl();
		UserDAOImpl daoUser = new UserDAOImpl();
		File serverFile = null;
		if (file == null) {
			String recordId = record.getUuid();
			daoRec.update(record);
			record = daoRec.readByKey(recordId);
			return record;
		} else {
			try {
				User user = daoUser.readByKey(record.getUserId());
				String url = UserFolderForPersonalData.getFolderForUser(user.getNickName());
				serverFile = new File(url + File.separator + record.getSupplement());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(file);
				stream.close();
				record.setSupplement(record.getSupplement());
				daoRec.update(record);
				LOG.info("You successfully uploaded file=" + record.getSupplement());
			} catch (IOException e) {
				LOG.error("You failed to upload " + record.getSupplement() + " => " + e.getMessage());
			}
			record = daoRec.readByKey(record.getUuid());
			return record;
		}
	}

	@Override
	public List<User> getActiveUsers() {

		return userDAO.getActiveUsers();
	}

	@Override
	public User getUserByEmail(String email) {
		UserDAOImpl daoUser = new UserDAOImpl();
		User user = null;
		user = daoUser.getUserByEmail(email);
		return user;
	}
	
    @Override
    @WebMethod
    public int[][] getRecDate() {
        
        return recordDAOImpl.getRecordDate();
    }

}
