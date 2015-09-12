package com.softserve.tc.diary;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.RoleDAOImpl;
import com.softserve.tc.diary.entity.Role;

public class TestRoleDAO {
	private static Connection conn;
	private static PreparedStatement ps;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		conn = ConnectManager.getConnectionToTestDB();
		BufferedReader br = null;
		String scriptSQL;
		StringBuilder sbResult = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader("./PostgreSQL_DB/DiaryTest.sql"));
			while ((scriptSQL = br.readLine()) != null) {
				sbResult.append(scriptSQL);
				sbResult.append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String result = new String(sbResult);
		ps = conn.prepareStatement(result);
		ps.execute();
	}

	@Before
	public void beforeTest() {
		String insertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
				+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', 2, 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, 2);"
				+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', 1, 'dfhfght@gmail.com', 'vfjukiuu', 'F', '1990-03-08', null, 1);"
				+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', 3, 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'M', '1989-02-20', null, 2);"
				+ "insert into record_list values('1','1','2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'public');"
				+ "insert into record_list values('2','3','2015-05-20 12:00:56','skjdhugfkdxufge',null,'private');"
				+ "insert into record_list values('3','1','2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'public');"
				+ "insert into tag values('1','#cars');" + "insert into tag values('3','#family');"
				+ "insert into tag values('2','#Love');" + "insert into tag values('4','#murderrrrrr');"
				+ "insert into tag_record values('1','1', '2');" + "insert into tag_record values('2','3', '1');"
				+ "insert into tag_record values('3','2', '3');";
		try {
			ps = conn.prepareStatement(insertData);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@After
	public void afterTest() {
		String deleteData = "delete from tag_record;" + "delete from record_list;" + "delete from user_card;"
				+ "delete from address;" + "delete from role;" + "delete from tag;";
		try {
			ps = conn.prepareStatement(deleteData);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateRole() {
		RoleDAOImpl roleDAO = new RoleDAOImpl();

		roleDAO.create(new Role("Administrator"));
		Role role = new Role();
		try {
			ps = conn.prepareStatement("SELECT name FROM role WHERE name ='Administrator'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				role.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertEquals("Administrator", role.getName());

	}

}
