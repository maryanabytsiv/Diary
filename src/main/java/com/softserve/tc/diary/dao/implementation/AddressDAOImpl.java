package com.softserve.tc.diary.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.connectionmanager.ConnectionManager;
import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant.Addresss;

public class AddressDAOImpl implements AddressDAO {

	private PreparedStatement ps;
	private Logger logger = Log.init(this.getClass().getName());
	private static ConnectionManager connection = null;

	private static AddressDAOImpl addresDAO = null;

	private AddressDAOImpl() {
	}

	public static AddressDAOImpl getInstance(ConnectionManager connect) {

		if (addresDAO == null) {
			addresDAO = new AddressDAOImpl();
			connection = connect;
		}
		return addresDAO;

	}

	public String create(Address object) {
		logger.debug("Creating address");
		String uuid = UUID.randomUUID().toString();
		try (Connection conn = connection.getConnection()) {
			ps = conn
					.prepareStatement("insert into address(id, country, city, street, build_number) values(?,?,?,?,?)");
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
		try (Connection conn = connection.getConnection()) {
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
		try (Connection conn = connection.getConnection()) {

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
			} finally {
				conn.setAutoCommit(true);
			}

		} catch (SQLException e) {
			logger.error("Can't update address", e);
		}
	}

	public void delete(Address object) {

		try (Connection conn = connection.getConnection()) {
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
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			logger.error("Can't delete address", e);
		}
	}

	public List<Address> getAll() {

		List<Address> list = new ArrayList<Address>();
		logger.debug("Get all address records");
		try (Connection conn = connection.getConnection()) {
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

	public Map<String, Integer> getCountSameAddresses(String mapping) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		logger.debug("Get  address and count same addresses");
		try (Connection conn = connection.getConnection()) {
			if (mapping.equalsIgnoreCase("city")) {
				ps = conn.prepareStatement("select country, city, COUNT(id) as count from user_card "
						+ "left join address ON id = address_id group by country, city;");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					map.put(rs.getString(Addresss.COUNTRY) + " " +
				rs.getString(Addresss.CITY), rs.getInt("count"));
				}
			} else if (mapping.equalsIgnoreCase("country")) {
				ps = conn.prepareStatement("select country, COUNT(id) as count from user_card "
						+ "left join address ON id = address_id group by country;");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					map.put(rs.getString(Addresss.COUNTRY), rs.getInt("count"));
				}
			} else {
				ps = conn.prepareStatement("select country, city, street, build_number from user_card "
						+ "left join address ON id = address_id;");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					map.put(rs.getString(Addresss.COUNTRY) +" "+ rs.getString(Addresss.CITY) + " " 
				+ rs.getString(Addresss.STREET) + " " + rs.getString(Addresss.BUILDNUMBER), null);
				}

			}
		} catch (SQLException e) {
			logger.error("Can't getAll address", e);
		}
		return map;
	}

	public List<Object> getDataForGeoChartGraphic(String country) {
		List<Object> list = new ArrayList<Object>();
		try (Connection conn = connection.getConnection()) {
			if (country == null || country.equalsIgnoreCase("world")) {
				ps = conn.prepareStatement(
						"select country, COUNT(session) as opensessions, COUNT(country) as allusers "
						+ " from user_card left join  address ON id = address_id " 
						+ " group by country order by country;");
			} else {
				ps = conn.prepareStatement(
						"select city, COUNT(session) as opensessions, COUNT(city) as allusers "			
						+ " from user_card left join  address ON id = address_id " 
						+ " where country like '" + country + "' "
						+ " group by city order by city;");
			}
			ResultSet rs = ps.executeQuery();
			List<String> title = new ArrayList<String>();
			List<Integer> active = new ArrayList<Integer>();
			List<Integer> all = new ArrayList<Integer>();
			while (rs.next()) {
				title.add(rs.getString(1));
				active.add(rs.getInt(2));
				all.add(rs.getInt(3));
			}
			list.add(title);
			list.add(active);
			list.add(all);
		} catch (SQLException e) {
			logger.error("Can't getAll address", e);
		}
		return list;
	}
	

	
}

