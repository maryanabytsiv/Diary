package com.softserve.tc.diary.dao.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.Role;
import com.softserve.tc.diary.entity.Sex;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.PasswordHelper;
import com.sun.xml.stream.xerces.util.ParserConfigurationSettings;

public class UserDAOImplTest {
    private Logger logger = Log.init(this.getClass().getName());
    private PreparedStatement ps = null;

    private static ConnectionManager conn = ConnectionManager.getInstance(DataBaseTest.TESTDB);
	
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException {
    	DBCreationManagerHelper.setUpBeforeClass();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
    	DBCreationManagerHelper.DropTableIfExists();
    }
    
    @Before
    public void beforeTest() throws SQLException {
    	DBCreationManagerHelper.insertValue();
    }
    
    @After
    public void afterTest() throws SQLException {
    	DBCreationManagerHelper.deleteAllFromTable();
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserNullPointerException() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        userDAO.create(new User(null, "Andriy", "Mural",
                null, null, "64561", Sex.FEMALE,
                "1999-03-02", "folder/folder/image.png", null));
    }
    
    @Test
    public void testCreateUser() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        userDAO.create(new User("hary12", "Andriy", "Mural",
                null, "bg@gmail.com", "64561",
                Sex.MALE, "1995-03-02", "folder/folder/image.png",
                Role.USER));
        User userActual = new User();
        try (Connection connection = conn.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name ='hary12';");
                ResultSet rs = ps.executeQuery();
                userActual = resultSet(rs);
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(userActual);
        assertEquals("hary12", userActual.getNickName());
        assertEquals("bg@gmail.com", userActual.geteMail());
        try {
            assertEquals(PasswordHelper.encrypt("64561"),
                    userActual.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assertEquals("MALE", userActual.getSex());
        assertEquals("USER", userActual.getRole());
        logger.info("test create user");
    }
    
    @Test
    public void testUpdateUser() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        User user = new User();
        user.setNickName("Test");
        user.setPassword("Test");
        user.seteMail("mail@mail.com");
        user.setSex("FEMALE");
        user.setRole("USER");
        user.setDateOfBirth("1999-10-10");
        userDAO.create(user);
        user.setFirstName("IRA");
        user.setSecondName("BLLLLL");
        user.setAddress(new Address("Poland", "Gdansk", "Naberejna", "52"));
        user.setAvatar("image.png");
        userDAO.update(user);
        User userActual = new User();
        try (Connection connection = conn.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
                ps.setString(1, user.getNickName());
                ResultSet rs = ps.executeQuery();
                userActual = resultSet(rs);
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(userActual);
        assertEquals("Test", userActual.getNickName());
        assertEquals("IRA", userActual.getFirstName());
        assertEquals("BLLLLL", userActual.getSecondName());
        assertEquals("mail@mail.com", userActual.geteMail());
        assertEquals("0cbc6611f5540bd0809a388dc95a615b", userActual.getPassword());
        ;
        assertEquals("FEMALE", userActual.getSex());
        assertEquals("1999-10-10", userActual.getDateOfBirth());
        assertEquals("USER", userActual.getRole());
        logger.info("test update user");
    }
    
    @Test
    public void testDeleteUser() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        User user = new User("delete", "Natalya", "Bolyk",
                new Address("Poland", "Gdansk", "Naberejna", "52"),
                "bg@gmail.com", "64561", Sex.FEMALE, null,
                "jfhfff.mvn", Role.ADMIN);
        userDAO.create(user);
        userDAO.delete(user);
        assertNull(userDAO.readByNickName("delete"));
        logger.info("test delete user");
    }
    
    @Test
    public void testGetAll() {
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        User user =
                new User("Bozo", "Oleg", "Ponkin",
                        new Address("Poland", "Gdansk", "Naberejna", "52"),
                        "bsss@gmail.com", "64561", Sex.MALE,
                        null, "jsjwe.txt", Role.ADMIN);
        userDAO.create(user);
        int actual = userDAO.getAll().size();
        int expected = 4;
        assertEquals(expected, actual);
        logger.info("test get all");
    }
    
    @Test
    public void testGetByNickName() {
        User userActual = new User();
        UserDAOImpl userDAO = UserDAOImpl.getInstance(conn);
        User user = new User("Bobik", "Oleg", "Ponkin",
                new Address("Poland", "Gdansk", "Naberejna", "52"),
                "bsss@gmail.com", "kjhgyiuu",
                Sex.MALE, null, "jsjwe.txt", Role.ADMIN);
        userDAO.create(user);
        try (Connection connection = conn.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(
                        "select  * from user_card left join address on (address.id=user_card.address_id) where user_card.nick_name =?;");
                ps.setString(1, "Bobik");
                ResultSet rs = ps.executeQuery();
                userActual = resultSet(rs);
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNotNull(userActual);
        assertEquals(user.getNickName(), userActual.getNickName());
        assertEquals(user.geteMail(), userActual.geteMail());
        try {
            assertEquals(PasswordHelper.encrypt(user.getPassword()),
                    userActual.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assertEquals(user.getSex(), userActual.getSex());
        assertEquals(user.getRole(), userActual.getRole());
        logger.info("test get by nickname");
    }
    
    @Test
    public void testreadByKey() {
        UserDAOImpl dao = UserDAOImpl.getInstance(conn);
        User user = dao.readByKey("1");
        assertNotNull(user);
        assertEquals(user.getNickName(), "BigBunny");
        assertEquals(user.getFirstName(), "Oleg");
        assertEquals(user.getSecondName(), "Pavliv");
        assertEquals(user.getAddress().toString(), "USA NC timesquare 5");
        assertEquals(user.geteMail(), "hgdf@gmail.com");
        try {
            assertEquals(PasswordHelper.encrypt("kdfhgrr"),
                    PasswordHelper.encrypt(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assertEquals(user.getSex(), "MALE");
        assertEquals(user.getDateOfBirth(), "1992-02-02");
        assertEquals(user.getAvatar(), null);
        assertEquals(user.getRole(), "USER");
        logger.info("test get by id");
    }
    
    @Test
    public void testCountAllBySex() {
        UserDAOImpl user = UserDAOImpl.getInstance(conn);
        int count = user.countAllBySex("MALE");
        assertEquals(2, count);
    }
    
    @Test
    public void testGetUsersByRole() {
        UserDAOImpl dao = UserDAOImpl.getInstance(conn);
        List<User> list = dao.getUsersByRole(Role.USER);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testGetByDateOfBirth() {
        UserDAOImpl user = UserDAOImpl.getInstance(conn);
        List<User> list = user.getByYearOfBirth("1989");
        int actual = list.size();
        int expected = 1;
        assertEquals(expected, actual);
    }
    
    private User resultSet(ResultSet rs) {
        User user = null;
        Address address = null;
        try {
            while (rs.next()) {
                address = new Address();
                address.setUuid(rs.getString("id"));
                address.setCountry(rs.getString("country"));
                address.setCity(rs.getString("city"));
                address.setStreet(rs.getString("street"));
                address.setBuildNumber(rs.getString("build_number"));
                user = new User();
                user.setNickName(rs.getString("nick_name"));
                user.setFirstName(rs.getString("first_name"));
                user.setSecondName(rs.getString("second_name"));
                user.setAddress(address);
                user.seteMail(rs.getString("e_mail"));
                user.setPassword(rs.getString("password"));
                user.setSex(rs.getString("Sex"));
                user.setDateOfBirth(rs.getString("date_of_birth"));
                user.setAvatar(rs.getString("avatar"));
                user.setRole(rs.getString("role"));
                user.setSession(rs.getString("session"));
            }
        } catch (SQLException e) {
            logger.error("ResultSet failed", e);
        }
        return user;
    }
    
}
