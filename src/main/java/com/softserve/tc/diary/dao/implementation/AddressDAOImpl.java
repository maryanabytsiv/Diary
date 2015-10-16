package com.softserve.tc.diary.dao.implementation;

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
import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.Addresss;

public class AddressDAOImpl implements AddressDAO {
    private PreparedStatement ps;
    private Logger logger = Log.init(this.getClass().getName());
    private ConnectionManager connectionManager = null;
    
    public AddressDAOImpl() {
        this.connectionManager = DBConnectionManager.getInstance(true);
    }
    
    public AddressDAOImpl(ConnectionManager conn) {
        this.connectionManager = conn;
    }
    
    public String create(Address object) {
        logger.debug("Creating address");
        String uuid=UUID.randomUUID().toString();
        try (Connection conn = connectionManager.getConnection()) {
            ps = conn
                    .prepareStatement(
                            "insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
            ps.setString(1, uuid);
            ps.setString(2, object.getCountry());
            ps.setString(3, object.getCity());
            ps.setString(4, object.getStreet());
            ps.setString(5, object.getBuildNumber());
            ps.execute();
            ps.close();
            logger.debug("Address created");
        } catch (SQLException e) {
            logger.error("Creating address failed", e);
        }
        return uuid;
    }
    
    public Address readByKey(String id) {
        
        Address address = null;
        logger.debug("Reading by key");
        try (Connection conn = connectionManager.getConnection()) {
            ps = conn.prepareStatement("select * from address where id=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                address = new Address(rs.getString(Addresss.COUNTRY), rs.getString(Addresss.CITY),
                        rs.getString(Addresss.STREET), rs.getString(Addresss.BUILDNUMBER));
            }
            logger.debug("ReadByKey done");
        } catch (SQLException e) {
            logger.error("Can't select from Address", e);
        }
        return address;
    }
    
    public void update(Address object) {
        
        logger.debug("Updating address");
        try (Connection conn = connectionManager.getConnection()) {
            
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update address set country = ?, city = ? ,street = ?, build_number = ?  where id = ?");
                ps.setString(1, object.getCountry());
                ps.setString(2, object.getCity());
                ps.setString(3, object.getStreet());
                ps.setString(4, object.getBuildNumber());
                ps.setString(5, object.getUuid());
                ps.execute();
                ps.close();
                logger.debug("Address updated");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
            
        } catch (SQLException e) {
            logger.error("Can't update address", e);
        }
    }
    
    public void delete(Address object) {
        
        try (Connection conn = connectionManager.getConnection()) {
            try {
                conn.setAutoCommit(false);
                logger.debug("Deleting address");
                ps = conn.prepareStatement(
                        "delete from address where country = ? and city = ? and street = ? and build_number = ?");
                ps.setString(1, object.getCountry());
                ps.setString(2, object.getCity());
                ps.setString(3, object.getStreet());
                ps.setString(4, object.getBuildNumber());
                ps.execute();
                logger.debug("Address deleted");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            }finally{
                conn.setAutoCommit(true);              
            }
        } catch (SQLException e) {
            logger.error("Can't delete address", e);
        }
    }
    
    public List<Address> getAll() {
        
        List<Address> list = new ArrayList<Address>();
        logger.debug("Get all address records");
        try (Connection conn = connectionManager.getConnection()) {
            ps = conn.prepareStatement("select * from address;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Address(rs.getString(Addresss.COUNTRY), rs.getString(Addresss.CITY),
                        rs.getString(Addresss.STREET), rs.getString(Addresss.BUILDNUMBER)));
            }
        } catch (SQLException e) {
            logger.error("Can't getAll address", e);
        }
        return list;
    }
}
