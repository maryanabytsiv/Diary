package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.entity.Address;

public class TestAddressDAO {


    @BeforeClass
    public static void setUpBeforeClass() throws SQLException {
    	Query.setUpBeforeClass();
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
    	Query.DropTableIfExists();
    }

    @Test
    public void testCreateAddress() {

        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address newAddress = new Address("Ukraine", "IF", "street", 12);
        addressDAO.create(newAddress);
        Address address = null;

        try {
            Query.ps = Query.connection.prepareStatement(
                    "select * from address where country = ? and city = ? and street = ? and build_number = ?");
            Query.ps.setString(1, newAddress.getCountry());
            Query.ps.setString(2, newAddress.getCity());
            Query.ps.setString(3, newAddress.getStreet());
            Query.ps.setInt(4, newAddress.getBuild_number());
            ResultSet rs = Query.ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotNull(address);
        assertEquals("Ukraine", address.getCountry());
        assertEquals("IF", address.getCity());
        assertEquals("street", address.getStreet());
        assertEquals(12, address.getBuild_number());
    }

    @Test
    public void testUpdateAddress() {
        
        
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        
        Address add = new Address("jhbkjhbkj", "fdfsfy", "Nsfsfft", 16);
        add.setId(addressDAO.getGeneratedId());
        try {
        	Query.ps = Query.connection.prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
        	Query.ps.setString(1, add.getId());
        	Query.ps.setString(2, add.getCountry());
        	Query.ps.setString(3, add.getCity());
        	Query.ps.setString(4, add.getStreet());
        	Query.ps.setInt(5, add.getBuild_number());
        	Query.ps.execute();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        addressDAO.update(add);
        Address address = null;
        try {
        	Query.ps = Query.connection.prepareStatement("select * from address where id=?");
        	Query.ps.setString(1, add.getId());
            ResultSet rs = Query.ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                address.setId(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(add.getCountry(), address.getCountry());
        assertEquals(add.getCity(), address.getCity());
        assertEquals(add.getStreet(), address.getStreet());
        assertEquals(add.getBuild_number(), address.getBuild_number());

    }
    
    @Test
    public void TestDeleteAddress() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address address = new Address("Ukraine", "Lviv", "Pasternaka", 5);
        addressDAO.create(address);
        addressDAO.delete(address);
        assertNull(address.getId());
    }
    
    @Test
    public void TestGetAll() {
        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address address = new Address("Ukraine", "Lviv", "Pasternaka", 5);
        addressDAO.create(address);
        int actual = addressDAO.getAll().size();
        int expected = 2;
        assertEquals(expected, actual);
    }
    
}
