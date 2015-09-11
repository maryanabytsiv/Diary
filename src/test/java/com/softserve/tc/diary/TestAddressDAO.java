package com.softserve.tc.diary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.entity.Address;

public class TestAddressDAO {

    private static Connection conn;
    private static PreparedStatement ps;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        BufferedReader br = null;
        String scriptSQL;
        String result = "";
        try {
            br = new BufferedReader(new FileReader("./PostgreSQL_DB/DiaryTest.sql"));
            while ((scriptSQL = br.readLine()) != null) {
                result += scriptSQL + "\n";

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement(result);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
//        ps = conn.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
//                + "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;" + "DROP TABLE IF EXISTS role;"
//                + "DROP TABLE IF EXISTS tag;");
//        ps.execute();
        ps.close();
    }

    @Test
    public void testCreateAddress() {

        AddressDAOImpl addressDAO = new AddressDAOImpl();
        Address newAddress = new Address("Ukraine", "IF", "street", 12);
        addressDAO.create(newAddress);
        Address address = null;

        try {
            ps = conn.prepareStatement(
                    "select * from address where country = ? and city = ? and street = ? and build_number = ?");
            ps.setString(1, newAddress.getCountry());
            ps.setString(2, newAddress.getCity());
            ps.setString(3, newAddress.getStreet());
            ps.setInt(4, newAddress.getBuild_number());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
            ps = conn.prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
            ps.setString(1, add.getId());
            ps.setString(2, add.getCountry());
            ps.setString(3, add.getCity());
            ps.setString(4, add.getStreet());
            ps.setInt(5, add.getBuild_number());
            ps.execute();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        addressDAO.update(add);
        Address address = null;
        try {
            ps = conn.prepareStatement("select * from address where id=?");
            ps.setString(1, add.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                address.setId(rs.getString(1));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(add.getCountry(), address.getCountry());
        assertEquals(add.getCity(), address.getCity());
        assertEquals(add.getStreet(), address.getStreet());
        assertEquals(add.getBuild_number(), address.getBuild_number());

    }
}
