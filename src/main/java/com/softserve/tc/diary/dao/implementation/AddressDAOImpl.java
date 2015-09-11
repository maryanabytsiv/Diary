package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.User;

public class AddressDAOImpl extends BaseDAOImpl<Address>implements AddressDAO {

    public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    private static Connection conn = null;
    private static PreparedStatement ps;

    private static void getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(Address object) {

        getConnection();
        try {
            ps = conn.prepareStatement("insert into address(country, city, street, build_number) values(?,?,?,?)");
            ps.setString(1, object.getCountry());
            ps.setString(2, object.getCity());
            ps.setString(3, object.getStreet());
            ps.setInt(4, object.getBuild_number());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Address readByKey(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    public void update(Address object) {
        getConnection();
        try {
            
            ps = conn.prepareStatement(
                    "update address set country = ?, city = ?, street = ?, build_number = ? where id = ?");
            ps.setString(1, object.getCountry());
            ps.setString(2, object.getCity());
            ps.setString(3, object.getStreet());
            ps.setInt(4, object.getBuild_number());
            ps.setInt(5, object.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(Address object) {
        // TODO Auto-generated method stub

    }

    public List<User> getUsersByCountry(String country) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<User> getUsersByCity(String city) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Address> getByCity(String city) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMostCommonCountry() {
        // TODO Auto-generated method stub
        return null;
    }

    public int countAllByCountry(String country) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int countAllByCity(String city) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int countAllByStreet(String city) {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<Address> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

}
