package com.softserve.tc.diary.dao.implementation;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.connectionmanager.DBConnectionManager;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.UserDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.Tag;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.Addresss;
import com.softserve.tc.diary.util.Constant.RecordList;
import com.softserve.tc.diary.util.Constant.UserCard;
import com.softserve.tc.diary.util.PasswordHelper;

public class UserDAOImpl implements UserDAO, BaseDAO<User> {
    private PreparedStatement ps = null;
    private ResultSet rs;
    private static Logger logger = Log.init("UserDAOImpl");
    private ConnectionManager connection = null;
    
    public UserDAOImpl() {
        this.connection = DBConnectionManager.getInstance(true);
    }
    
    public UserDAOImpl(ConnectionManager conn) {
        this.connection = conn;
    }
    
    public void create(User object) {
        
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                if ((object.getRole() == null) || (object.geteMail() == null)
                        || (object.getNickName() == null)) {
                    logger.error("You not enter nickname, e-mail or role");
                    throw new IllegalArgumentException();
                } else {
                    logger.debug("Creating user");
                    ps = conn.prepareStatement(
                            "insert into user_card(uid,nick_name,e_mail,password,role,sex) values(?,?,?,?,?,?);");
                    ps.setString(1, UUID.randomUUID().toString());
                    ps.setString(2, object.getNickName());
                    ps.setString(3, object.geteMail());
                    try {
						ps.setString(4,
						        PasswordHelper.encrypt(object.getPassword()));
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						logger.error("No such algorithm exception!", e);
					}
                    ps.setString(5, object.getRole().toUpperCase());
                    ps.setString(6, "MALE");
                    ps.execute();
                    ps.close();
                    conn.commit();
                }
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("create user failed", e);
        }
    }
    
    public void update(User object) {
        String[] splitAddress = object.getAddress().split(", ");
        Address newAdress = new Address(splitAddress[0], splitAddress[1],
                splitAddress[2], splitAddress[3]);
        AddressDAOImpl adressDAO = new AddressDAOImpl();
        
        User userToUpdate = readByNickName(object.getNickName());
        String addressUUID = "";
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "select address_id AS id from user_card where nick_name=?;");
            ps.setString(1, userToUpdate.getNickName());
            ps.execute();
            rs = ps.executeQuery();
            if (rs.next()) {
                addressUUID = rs.getString(Addresss.ID);
            }
            
        } catch (SQLException e) {
            logger.error("create address failed", e);
        }
        
        newAdress.setUuid(addressUUID);
        adressDAO.update(newAdress);
        
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update user_card set first_name=?,"
                                + " second_name=?, e_mail=?, password=?, sex=?,"
                                + " date_of_birth=CAST(? AS DATE), avatar=?,role=?, session = ? where nick_name=?;");
                ps.setString(1, object.getFirstName());
                ps.setString(2, object.getSecondName());
                ps.setString(3, object.geteMail());
                ps.setString(4, object.getPassword());
                ps.setString(5, object.getSex().toUpperCase());
                ps.setString(6, object.getDateOfBirth());
                ps.setString(7, object.getAvatar());
                ps.setString(8, object.getRole().toUpperCase());
                ps.setString(9, object.getSession());
                ps.setString(10, object.getNickName());
                ps.execute();
                conn.commit();
                logger.debug("User updated");
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("Update user failed", e);
        }
        
    }
    
    public void delete(User object) {
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                logger.debug("Deleting user");
                ps = conn.prepareStatement(
                        "delete from user_card where nick_name=?");
                ps.setString(1, object.getNickName());
                ps.execute();
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("delete user failed", e);
        }
        
    }
    
    public List<User> getAll() {
        List<User> list = new ArrayList<User>();
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id);");
                rs = ps.executeQuery();
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
            String address = "";
            while (rs.next()) {
                address = rs.getString(Addresss.COUNTRY) + ", " + rs.getString(Addresss.CITY)
                        + ", " + rs.getString(Addresss.STREET) + ", "
                        + rs.getString(Addresss.BUILDNUMBER);
                list.add(new User(rs.getString(UserCard.NICKNAME),
                        rs.getString(UserCard.FIRSTNAME), rs.getString(UserCard.SECONDNAME),
                        address, rs.getString(UserCard.EMAIL),
                        rs.getString(UserCard.PASSWORD),
                        Sex.valueOf(rs.getString(UserCard.SEX)),
                        rs.getString(UserCard.DATEOFBIRTH), rs.getString(UserCard.AVATAR),
                        Role.valueOf(rs.getString(UserCard.ROLE))));
            }
        } catch (SQLException e) {
            logger.error("select all failed", e);
        }
        return list;
    }
    
    public User readByKey(String uuid) {
        String query =
                "select * from user_card left join address on (address.id=user_card.address_id)"
                        + " where uid like '" + uuid + "';";
        User user = null;
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                user = resultSet(rs);
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("read by key failed", e);
        }
        return user;
    }
    
    public User readByNickName(String nickName) { // id as NICK_NAME
        User user = null;
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
                ps.setString(1, nickName);
                rs = ps.executeQuery();
                user = resultSet(rs);
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("read by key failed", e);
        }
        return user;
    }
    
    public int countAllBySex(String sex) {
        int users = 0;
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "select COUNT(*) AS uid from user_card where sex =? group by sex;");
            ps.setString(1, sex.toUpperCase());
            rs = ps.executeQuery();
            while (rs.next()) {
                users = rs.getInt(UserCard.UID);
            }
        } catch (SQLException e) {
            logger.error("Sorry, I can't count all by sex ;(", e);
        }
        return users;
    }
    
    public List<User> getByYearOfBirth(String yearOfBirth) {
        List<User> usersByYear = new ArrayList<User>();
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement(
                    "select * from user_card where extract (year from date_of_birth)=CAST(? AS double precision); ");
            ps.setString(1, yearOfBirth);
            rs = ps.executeQuery();
            while (rs.next()) {
                usersByYear.add(resultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("get by birthday failed", e);
        }
        return usersByYear;
    }
    
    public List<User> getUsersByRole(Role role) {
        List<User> list = new ArrayList<User>();
        String roleStr = role.toString();
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id)"
                                + "where role like '" + roleStr + "'");
                rs = ps.executeQuery();
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
            String address = "";
            while (rs.next()) {
                address = rs.getString(Addresss.COUNTRY) + ", " + rs.getString(Addresss.CITY)
                        + ", " + rs.getString(Addresss.STREET) + ", "
                        + rs.getString(Addresss.BUILDNUMBER);
                list.add(new User(rs.getString(UserCard.NICKNAME),
                        rs.getString(UserCard.FIRSTNAME), rs.getString(UserCard.SECONDNAME),
                        address, rs.getString(UserCard.EMAIL),
                        rs.getString(UserCard.PASSWORD),
                        Sex.valueOf(rs.getString(UserCard.SEX)),
                        rs.getString(UserCard.DATEOFBIRTH), rs.getString(UserCard.AVATAR),
                        Role.valueOf(rs.getString(UserCard.ROLE))));
            }
        } catch (SQLException e) {
            logger.error("select all failed by role", e);
        }
        return list;
    }
    
    private User resultSet(ResultSet rs) {
        User user = null;
        
        try {
            while (rs.next()) {
            	user = new User();
                user.setUuid(rs.getString(UserCard.UID));
                user.setNickName(rs.getString(UserCard.NICKNAME));
                user.setFirstName(rs.getString(UserCard.FIRSTNAME));
                user.setSecondName(rs.getString(UserCard.SECONDNAME));
                user.setAddress(rs.getString(Addresss.COUNTRY) + ", "
                        + rs.getString(Addresss.CITY) + ", " + rs.getString(Addresss.STREET)
                        + ", " + rs.getString(Addresss.BUILDNUMBER));
                user.seteMail(rs.getString(UserCard.EMAIL));
                user.setPassword(rs.getString(UserCard.PASSWORD));
                user.setSex(rs.getString(UserCard.SEX));
                user.setDateOfBirth(rs.getString(UserCard.DATEOFBIRTH));
                user.setAvatar(rs.getString(UserCard.AVATAR));
                user.setRole(rs.getString(UserCard.ROLE));
                user.setSession(rs.getString(UserCard.SESSION));
            }
        } catch (SQLException e) {
            logger.error("ResultSet failed", e);
        }
        return user;
    }
    public User getMostActiveUser() {
        User user = null;
        
        try (Connection conn = connection.getConnection()) {
            String query = "select count(*),user_id_rec from record_list group by user_id_rec having count(*)>1";
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String uuid="" ;
            int i=0;
            while (rs.next()) {
                i++;
                uuid = rs.getString(RecordList.USERIDREC);
                if(i==1){
                    break;
                }
            }
            UserDAOImpl userDAOImpl=new UserDAOImpl();
            user = userDAOImpl.readByKey(uuid);
        } catch (SQLException e) {
            logger.error("fail get most popular tag", e);
        }
        return user;
    }
    
    public int[] getSexStatistic(){
        int[] sexStatistic = new int[3];
        
        try (Connection conn = connection.getConnection()) {
            String query = "select count(*) AS uid from user_card where sex='MALE'";
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int male =0;
            int i =0;
            while(rs.next()){
                i++;
                male = rs.getInt(UserCard.UID);
                if (i==1){
                    break;
                }
            }
            String query2 = "select count(*) AS uid from user_card where sex='FEMALE'";
            ps = conn.prepareStatement(query2);
            ResultSet rs2 = ps.executeQuery();
            int female =0;
            i = 0;
            while(rs2.next()){
                i++;
                female = rs2.getInt(UserCard.UID);
                if (i==1){
                    break;
                }
            }
           
            sexStatistic[0]=male;
            sexStatistic[1]=female;
                   
            
        } catch (SQLException e) {
            logger.error("fail get sex stats", e);
        }
      
        return sexStatistic;
    }
}
