package com.softserve.tc.diary;

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

import com.softserve.tc.diary.connectmanager.TestDBConnection;
import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.log.Log;

public class TestAddressDAO {
	private Logger logger = Log.init(this.getClass().getName());
	private PreparedStatement ps = null;

	@BeforeClass
	public static void setUpBeforeClass() throws SQLException {
		DBCreationManager.setUpBeforeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws SQLException {
		DBCreationManager.DropTableIfExists();
	}

	@Before
	public void beforeTest() throws SQLException {
		DBCreationManager.insertValue();
	}

	@After
	public void afterTest() throws SQLException {
		DBCreationManager.deleteAllFromTable();
	}

	@Test
	public void testCreateAddress() {
		AddressDAOImpl addressDAO = new AddressDAOImpl();
		Address newAddress = new Address("Ukraine", "IF", "street", "12");
		addressDAO.create(newAddress);
		Address address = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection.prepareStatement(
					"select * from address where country = ? and city = ? and street = ? and build_number = ?");
			ps.setString(1, newAddress.getCountry());
			ps.setString(2, newAddress.getCity());
			ps.setString(3, newAddress.getStreet());
			ps.setString(4, newAddress.getBuild_number());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			ps.close();
		} catch (SQLException e) {
			logger.error("select failed", e);
		}

		assertNotNull(address);
		assertEquals("Ukraine", address.getCountry());
		assertEquals("IF", address.getCity());
		assertEquals("street", address.getStreet());
		assertEquals("12", address.getBuild_number());
		logger.info("Test create address");
	}

	@Test
	public void testUpdateAddress() {
		AddressDAOImpl addressDAO = new AddressDAOImpl();
		Address add = new Address("jhbkjhbkj", "fdfsfy", "Nsfsfft", "16");
		add.setId(UUID.randomUUID().toString());
		try (Connection connection = TestDBConnection.getConnection()) {
			try {
				connection.setAutoCommit(false);
				ps = connection.prepareStatement(
						"insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
				ps.setString(1, add.getId());
				ps.setString(2, add.getCountry());
				ps.setString(3, add.getCity());
				ps.setString(4, add.getStreet());
				ps.setString(5, add.getBuild_number());
				ps.execute();
				ps.close();
				connection.commit();
			} catch (SQLException e) {
				logger.error("Error. Rollback changes", e);
				connection.rollback();
				connection.setAutoCommit(true);
			}
			connection.setAutoCommit(true);
		} catch (SQLException e1) {
			logger.error("insert failed", e1);
		}
		addressDAO.update(add);

		Address address = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection.prepareStatement("select * from address where id=?");
			ps.setString(1, add.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
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
		Address address = new Address("11111111", "Kiev", "Pasternaka", "15");
		addressDAO.create(address);
		addressDAO.delete(address);
		Address deleteAddress = null;
		try (Connection connection = TestDBConnection.getConnection()) {
			ps = connection.prepareStatement(
					"select * from address where country = ? and city = ? and street = ? and build_number = ?");
			ps.setString(1, address.getCountry());
			ps.setString(2, address.getCity());
			ps.setString(3, address.getStreet());
			ps.setString(4, address.getBuild_number());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				deleteAddress = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				deleteAddress.setId(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("select failed", e);
		}
		assertNull(deleteAddress);
		logger.info("Test delete address");
	}

	@Test
	public void TestGetAll() {
		AddressDAOImpl addressDAO = new AddressDAOImpl();
		Address address = new Address("Ukraine", "Odessa", "Pasternaka", "5");
		addressDAO.create(address);
		int actual = addressDAO.getAll().size();
		int expected = 4;
		assertEquals(expected, actual);
		logger.info("Test getAll");
	}

}
