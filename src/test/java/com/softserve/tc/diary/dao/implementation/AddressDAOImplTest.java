package com.softserve.tc.diary.dao.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.Addresss;

public class AddressDAOImplTest {
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
    
    @Test
    public void testCreateAddress() {
        AddressDAOImpl addressDAO = AddressDAOImpl.getInstance(conn);
        Address newAddress = new Address("Ukraine", "IF", "street", "12");
        Address address = null;
        try (Connection connection = conn.getConnection()) {
            addressDAO.create(newAddress);
            ps = connection.prepareStatement(
                    "select * from address where country = ? and city = ? and street = ? and build_number = ?");
            ps.setString(1, newAddress.getCountry());
            ps.setString(2, newAddress.getCity());
            ps.setString(3, newAddress.getStreet());
            ps.setString(4, newAddress.getBuildNumber());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(Addresss.COUNTRY), rs.getString(Addresss.CITY),
                        rs.getString(Addresss.STREET), rs.getString(Addresss.BUILDNUMBER));
            }
            ps.close();
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        
        assertNotNull(address);
        assertEquals("Ukraine", address.getCountry());
        assertEquals("IF", address.getCity());
        assertEquals("street", address.getStreet());
        assertEquals("12", address.getBuildNumber());
        logger.info("Test create address");
    }
    
    @Test
    public void testUpdateAddress() {
        AddressDAOImpl addressDAO = AddressDAOImpl.getInstance(conn);
        Address add = new Address("jhbkjhbkj", "fdfsfy", "Nsfsfft", "16");
        add.setUuid(UUID.randomUUID().toString());
        try (Connection connection = conn.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(
                        "insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
                ps.setString(1, add.getUuid());
                ps.setString(2, add.getCountry());
                ps.setString(3, add.getCity());
                ps.setString(4, add.getStreet());
                ps.setString(5, add.getBuildNumber());
                ps.execute();
                ps.close();
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
            }finally{
                connection.setAutoCommit(true);              
            }
        } catch (SQLException e1) {
            logger.error("insert failed", e1);
        }
        addressDAO.update(add);
        
        Address address = null;
        try (Connection connection = conn.getConnection()) {
            ps = connection
                    .prepareStatement("select * from address where id=?");
            ps.setString(1, add.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(Addresss.COUNTRY), rs.getString(Addresss.CITY),
                        rs.getString(Addresss.STREET), rs.getString(Addresss.BUILDNUMBER));
                address.setUuid(rs.getString(Addresss.ID));
            }
            
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertEquals(add.getCountry(), address.getCountry());
        assertEquals(add.getCity(), address.getCity());
        assertEquals(add.getStreet(), address.getStreet());
        assertEquals(add.getBuildNumber(), address.getBuildNumber());
        logger.info("Test update address");
    }
    
    @Test
    public void testDeleteAddress() {
        AddressDAOImpl addressDAO = AddressDAOImpl.getInstance(conn);
        Address address = new Address("11111111", "Kiev", "Pasternaka", "15");
        addressDAO.create(address);
        
        Address deleteAddress = null;
        try (Connection connection = conn.getConnection()) {
            addressDAO.delete(address);
            ps = connection.prepareStatement(
                    "select * from address where country = ? and city = ? and street = ? and build_number = ?");
            ps.setString(1, address.getCountry());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getStreet());
            ps.setString(4, address.getBuildNumber());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deleteAddress = new Address(rs.getString(Addresss.COUNTRY), rs.getString(Addresss.CITY),
                        rs.getString(Addresss.STREET), rs.getString(Addresss.BUILDNUMBER));
                deleteAddress.setUuid(rs.getString(Addresss.ID));
            }
        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertNull(deleteAddress);
        logger.info("Test delete address");
    }
    
    @Test
    public void TestGetAll() {
        AddressDAOImpl addressDAO = AddressDAOImpl.getInstance(conn);
        Address address = new Address("Ukraine", "Odessa", "Pasternaka", "5");
        addressDAO.create(address);
        int actual = addressDAO.getAll().size();
        int expected = 4;
        assertEquals(expected, actual);
        logger.info("Test getAll");
    }
    
}
