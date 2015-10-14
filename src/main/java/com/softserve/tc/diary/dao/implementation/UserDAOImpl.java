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
    
    public String create(User object) {
        String uuid = UUID.randomUUID().toString();
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
                    ps.setString(1, uuid);
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
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("create user failed", e);
        }
        return uuid;
    }
    
    public void update(User object) {
        Address newAddress = object.getAddress();
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        
        String addressUuid = "";
        if (newAddress.getUuid().isEmpty()) {
            addressUuid = addressDAO.create(newAddress);
        } else {
            addressUuid = newAddress.getUuid();
            addressDAO.update(newAddress);
        }
        
        String avatar = "";
        if (object.getAvatar().isEmpty()) {
            User userFromDB = readByKey(object.getUuid());
            avatar = userFromDB.getAvatar();
        } else {
            avatar = object.getAvatar();
        }
        
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update user_card set first_name=?,"
                                + " second_name=?, address_id=?, e_mail=?, password=?, sex=?,"
                                + " date_of_birth=CAST(? AS DATE), avatar=?,role=?, session = ? where nick_name=?;");
                ps.setString(1, object.getFirstName());
                ps.setString(2, object.getSecondName());
                ps.setString(3, addressUuid);
                ps.setString(4, object.geteMail());
                ps.setString(5, object.getPassword());
                ps.setString(6, object.getSex().toUpperCase());
                if (object.getDateOfBirth().isEmpty()) {
                    ps.setString(7, null);
                } else {
                    ps.setString(7, object.getDateOfBirth());
                }
                ps.setString(8, avatar);
                ps.setString(9, object.getRole().toUpperCase());
                ps.setString(10, object.getSession());
                ps.setString(11, object.getNickName());
                ps.execute();
                conn.commit();
                logger.debug("User updated");
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            } finally {
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
            } finally {
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
            } finally {
                conn.setAutoCommit(true);
            }
            Address address = null;
            while (rs.next()) {
                address = new Address();
                address.setUuid(rs.getString(Addresss.ID));
                address.setCountry(rs.getString(Addresss.COUNTRY));
                address.setCity(rs.getString(Addresss.CITY));
                address.setStreet(rs.getString(Addresss.STREET));
                address.setBuildNumber(rs.getString(Addresss.BUILDNUMBER));
                
                list.add(new User(rs.getString(UserCard.NICKNAME),
                        rs.getString(UserCard.FIRSTNAME),
                        rs.getString(UserCard.SECONDNAME),
                        address, rs.getString(UserCard.EMAIL),
                        rs.getString(UserCard.PASSWORD),
                        Sex.valueOf(rs.getString(UserCard.SEX)),
                        rs.getString(UserCard.DATEOFBIRTH),
                        rs.getString(UserCard.AVATAR),
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
            } finally {
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
            } finally {
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
            } finally {
                conn.setAutoCommit(true);
            }
            Address address = null;
            while (rs.next()) {
                address = new Address();
                address.setUuid(rs.getString(Addresss.ID));
                address.setCountry(rs.getString(Addresss.COUNTRY));
                address.setCity(rs.getString(Addresss.CITY));
                address.setStreet(rs.getString(Addresss.STREET));
                address.setBuildNumber(rs.getString(Addresss.BUILDNUMBER));
                
                list.add(new User(rs.getString(UserCard.NICKNAME),
                        rs.getString(UserCard.FIRSTNAME),
                        rs.getString(UserCard.SECONDNAME),
                        address, rs.getString(UserCard.EMAIL),
                        rs.getString(UserCard.PASSWORD),
                        Sex.valueOf(rs.getString(UserCard.SEX)),
                        rs.getString(UserCard.DATEOFBIRTH),
                        rs.getString(UserCard.AVATAR),
                        Role.valueOf(rs.getString(UserCard.ROLE))));
            }
        } catch (SQLException e) {
            logger.error("select all failed by role", e);
        }
        return list;
    }
    
    private User resultSet(ResultSet rs) {
        User user = null;
        Address address = null;
        try {
            while (rs.next()) {
                address = new Address();
                address.setUuid(rs.getString(Addresss.ID));
                address.setCountry(rs.getString(Addresss.COUNTRY));
                address.setCity(rs.getString(Addresss.CITY));
                address.setStreet(rs.getString(Addresss.STREET));
                address.setBuildNumber(rs.getString(Addresss.BUILDNUMBER));
                
                user = new User();
                user.setUuid(rs.getString(UserCard.UID));
                user.setNickName(rs.getString(UserCard.NICKNAME));
                user.setFirstName(rs.getString(UserCard.FIRSTNAME));
                user.setSecondName(rs.getString(UserCard.SECONDNAME));
                user.setAddress(address);
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
            String query =
                    "select count(*),user_id_rec from record_list group by user_id_rec having count(*)>1";
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            String uuid = "";
            int i = 0;
            while (rs.next()) {
                i++;
                uuid = rs.getString(RecordList.USERIDREC);
                if (i == 1) {
                    break;
                }
            }
            UserDAOImpl userDAOImpl = new UserDAOImpl();
            user = userDAOImpl.readByKey(uuid);
        } catch (SQLException e) {
            logger.error("fail get most popular tag", e);
        }
        return user;
    }
    
    public int[] getSexStatistic() {
        int[] sexStatistic = new int[3];
        
        try (Connection conn = connection.getConnection()) {
            String query =
                    "select count(*) AS uid from user_card where sex='MALE'";
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int male = 0;
            int i = 0;
            while (rs.next()) {
                i++;
                male = rs.getInt(UserCard.UID);
                if (i == 1) {
                    break;
                }
            }
            String query2 =
                    "select count(*) AS uid from user_card where sex='FEMALE'";
            ps = conn.prepareStatement(query2);
            ResultSet rs2 = ps.executeQuery();
            int female = 0;
            i = 0;
            while (rs2.next()) {
                i++;
                female = rs2.getInt(UserCard.UID);
                if (i == 1) {
                    break;
                }
            }
            
            sexStatistic[0] = male;
            sexStatistic[1] = female;
            
        } catch (SQLException e) {
            logger.error("fail get sex stats", e);
        }
        
        return sexStatistic;
    }
}
