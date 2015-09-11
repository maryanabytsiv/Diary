package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.User;

public class AddressDAOImpl implements AddressDAO, IdGenerator {

    private static Connection conn = null;
    private static PreparedStatement ps;

    public void create(Address object) {
        
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
            ps.setString(1, getGeneratedId());
            ps.setString(2, object.getCountry());
            ps.setString(3, object.getCity());
            ps.setString(4, object.getStreet());
            ps.setInt(5, object.getBuild_number());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Address readByKey(String id) {
        Address address = null;
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("select * from address where id=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void update(Address object) {
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement(
                    "update address set country = ? where id = ?");
            ps.setString(1, object.getCountry());
            ps.setString(2, object.getId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(Address object) {
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("delete from address where id=?");
            ps.setString(1, object.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        List<Address> list = new ArrayList<Address>();
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("select * from address");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getGeneratedId() {
        UUID idOne = UUID.randomUUID();
        return idOne.toString();
    }

}
