package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;

import com.softserve.tc.diary.ConnectManager;
import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.log.Log;

public class AddressDAOImpl implements AddressDAO {

    private static Connection conn = null;
    private static PreparedStatement ps;
    Logger logger = Log.init(this.getClass().getName());

    public void create(Address object) {
        logger.debug("Creating address");
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn
                    .prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, object.getCountry());
            ps.setString(3, object.getCity());
            ps.setString(4, object.getStreet());
            ps.setInt(5, object.getBuild_number());
            ps.execute();
            ps.close();
            logger.debug("Address created");
        } catch (SQLException e) {
            logger.error("Creating address failed", e);
        }
    }

    public Address readByKey(String id) {
        Address address = null;
        logger.debug("Reading by key");
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("select * from address where id=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
            logger.debug("ReadByKey done");
        } catch (SQLException e) {
            logger.error("Can't select from Address", e);
        }
        return address;
    }

    public void update(Address object) {
        logger.debug("Updating address");
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("update address set country = ? where id = ?");
            ps.setString(1, object.getCountry());
            ps.setString(2, object.getId());
            ps.execute();
            ps.close();
            logger.debug("Address updated");
        } catch (SQLException e) {
            logger.error("Can't update address", e);
        }
    }

    public void delete(Address object) {
        try {
            logger.debug("Deleting address");
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("delete from address where id=?");
            ps.setString(1, object.getId());
            ps.execute();
            logger.debug("Address deleted");
        } catch (SQLException e) {
            logger.error("Can't delete address", e);
        }
    }

    public List<Address> getAll() {
        List<Address> list = new ArrayList<Address>();
        logger.debug("Get all address records");
        try {
            conn = ConnectManager.getConnectionToTestDB();
            ps = conn.prepareStatement("select * from address");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Address(rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
        } catch (SQLException e) {
            logger.error("Can't getAll address", e);
        }
        return list;
    }
}
