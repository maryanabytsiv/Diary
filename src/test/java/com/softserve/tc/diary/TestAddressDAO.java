package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.log.Log;


public class TestAddressDAO {
    private Logger logger = Log.init(this.getClass().getName());

    @BeforeClass
    public static void setUpBeforeClass() throws SQLException {
        SQL_Statement.setUpBeforeClass();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        SQL_Statement.DropTableIfExists();
    }

	@Before
	public void beforeTest() throws SQLException {
		SQL_Statement.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		SQL_Statement.deleteAllFromTable();
	}

    @Test
    public void testCreateAddress() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address newAddress = new Address("Ukraine", "IF", "street", 12);
        addressDAO.create(newAddress);
        Address address = null;
        try {
            SQL_Statement.ps = SQL_Statement.connection.prepareStatement(
                    "select * from address where country = ? and city = ? and street = ? and build_number = ?");
            SQL_Statement.ps.setString(1, newAddress.getCountry());
            SQL_Statement.ps.setString(2, newAddress.getCity());
            SQL_Statement.ps.setString(3, newAddress.getStreet());
            SQL_Statement.ps.setInt(4, newAddress.getBuild_number());
            ResultSet rs = SQL_Statement.ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (SQLException e) {

            logger.error("select failed", e);
        }

        assertNotNull(address);
        assertEquals("Ukraine", address.getCountry());
        assertEquals("IF", address.getCity());
        assertEquals("street", address.getStreet());
        assertEquals(12, address.getBuild_number());
        logger.info("Test create address");
    }

    @Test
    public void testUpdateAddress() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address add = new Address("jhbkjhbkj", "fdfsfy", "Nsfsfft", 16);
        add.setId(UUID.randomUUID().toString());
        try {
            SQL_Statement.ps = SQL_Statement.connection
                    .prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
            SQL_Statement.ps.setString(1, add.getId());
            SQL_Statement.ps.setString(2, add.getCountry());
            SQL_Statement.ps.setString(3, add.getCity());
            SQL_Statement.ps.setString(4, add.getStreet());
            SQL_Statement.ps.setInt(5, add.getBuild_number());
            SQL_Statement.ps.execute();
        } catch (SQLException e1) {
            logger.error("insert failed", e1);
        }
        addressDAO.update(add);
        Address address = null;
        try {
            SQL_Statement.ps = SQL_Statement.connection.prepareStatement("select * from address where id=?");
            SQL_Statement.ps.setString(1, add.getId());
            ResultSet rs = SQL_Statement.ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                address.setId(rs.getString(1));
            }

        } catch (SQLException e) {
            logger.error("select failed", e);
        }
        assertEquals(add.getCountry(), address.getCountry());
        assertEquals(add.getCity(), address.getCity());
        assertEquals(add.getStreet(), address.getStreet());
        assertEquals(add.getBuild_number(), address.getBuild_number());
        logger.info("Test update address");
    }

    @Test
    public void TestDeleteAddress() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address address = new Address("Ukraine", "Lviv", "Pasternaka", 5);
        addressDAO.create(address);
        addressDAO.delete(address);
        assertNull(address.getId());
        logger.info("Test delete address");
    }

    @Test
    public void TestGetAll() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address address = new Address("Ukraine", "Lviv", "Pasternaka", 5);
        addressDAO.create(address);
        int actual = addressDAO.getAll().size();
        int expected = 4;
        assertEquals(expected, actual);
        logger.info("Test getAll");
    }

}
