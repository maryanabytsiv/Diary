package com.softserve.tc.diary;

import static org.junit.Assert.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.softserve.tc.diary.dao.implementation.RecordDAOImpl;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Status;


public class TestRecordDAO {

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
        ps = conn.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
                + "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;" + "DROP TABLE IF EXISTS role;"
                + "DROP TABLE IF EXISTS tag;");
        ps.execute();
        ps.close();
	}

	
	@Before
	 public void beforeTest() {
	  String isertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
	    + "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
	    + "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
	    + "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
	    + "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com', 'kdfhgrr', 'M', '1992-02-02', null, '2');"
	    + "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com', 'vfjukiuu', 'F', '1990-03-08', null, '1');"
	    + "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'M', '1989-02-20', null, '2');"
	    + "insert into record_list values('1','1','2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'public');"
	    + "insert into record_list values('2','3','2015-05-20 12:00:56','skjdhugfkdxufge',null,'private');"
	    + "insert into record_list values('3','1','2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'public');"
	    + "insert into tag values('1','#cars');" + "insert into tag values('3','#family');"
	    + "insert into tag values('2','#Love');" + "insert into tag values('4','#murderrrrrr');"
	    + "insert into tag_record values('1','1', '2');"
	    + "insert into tag_record values('2','3', '1');"
	    + "insert into tag_record values('3','2', '3');";
	  try {
	   ps = conn.prepareStatement(isertData);
	   ps.execute();
	  } catch (SQLException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	 }
	 @After
	 public void afterTest(){
	  String deleteData="delete from tag_record;"+"delete from record_list;"+"delete from user_card;"+"delete from address;"+"delete from role;"+"delete from tag;";
	    try {
	     ps = conn.prepareStatement(deleteData);
	     ps.execute();
	    } catch (SQLException e) {
	     // TODO Auto-generated catch block
	     e.printStackTrace();
	    }
	 }
	
	
	@Test
		public void testCreateRecord() {

		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		
		RecordDAOImpl RecordDAO = new RecordDAOImpl();
		Record newRecord = new Record( "1", createdTime, "#Hello, how are you??", "http:/ntiguwgni/gtrwgtwg/gwt", Status.PRIVATE );
		RecordDAO.create(newRecord);
		Record record = null;
		
		try {
			ps = conn.prepareStatement("select * from record_list where user_id_rec ='1';");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				record = new Record( rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5),Status.PRIVATE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(record.getCreated_time());
		assertNotNull(record);
		assertEquals("1", record.getUser_name());
		assertEquals(newRecord.getCreated_time(), record.getCreated_time());
		assertEquals("#Hello, how are you??", record.getText());
		assertEquals("http:/ntiguwgni/gtrwgtwg/gwt", record.getSupplement());
		assertEquals("PRIVATE" , record.getVisibility());
	}
	

	@Test
	public void testUpdateRecord() {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record rec = new Record("1", createdTime, "Nsfsfft","ferfre", Status.PRIVATE);
        rec.setId_rec(recordDAO.getGeneratedId());

        try {
            ps = conn.prepareStatement("insert into record_list values(?,?,CAST(? AS DATE),?,?,?)");
            ps.setString(1, rec.getId_rec());
            ps.setString(2, rec.getUser_name());
            ps.setTimestamp(3, rec.getCreated_time());
            ps.setString(4, rec.getText());
            ps.setString(5, rec.getSupplement());
            ps.setString(6, rec.getVisibility());
            ps.execute();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        recordDAO.update(rec);
        Record record = null;
        try {
            ps = conn.prepareStatement("select * from record_list where id_rec=?");
            ps.setString(1, rec.getId_rec());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	record = new Record(rs.getString(2), rs.getTimestamp(3), rs.getString(4), rs.getString(5),Status.valueOf(rs.getString(6)));
            	record.setId_rec(rs.getString(1));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(rec.getId_rec(), record.getId_rec());
        assertEquals(rec.getCreated_time(), record.getCreated_time());
        assertEquals(rec.getSupplement(), record.getSupplement());
        assertEquals(rec.getText(), record.getText());
        assertEquals(rec.getUser_name(), record.getUser_name());
        assertEquals(rec.getVisibility(), record.getVisibility());
	}

	
    @Test
    public void TestDeleteRecord() {
		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
		System.out.println(createdTime);
		
        RecordDAOImpl recordDAO = new RecordDAOImpl();
        Record record = new Record( "2", createdTime, "#Hello, how are you??", "http:/ntiguwgni/gtrwgtwg/gwt", Status.PRIVATE );
        recordDAO.create(record);
        recordDAO.delete(record);
        assertNull(recordDAO.getRecordByName("2"));
    }

//	@Test
//	public void testGetAll() {
//		Timestamp  createdTime = new Timestamp(new java.util.Date().getTime());
//
//		try {
//			ps = conn.prepareStatement("select * from record_list;");
//			ResultSet rs = ps.executeQuery();
//			
//			while (rs.next()){System.out.println(rs.getString("id_rec") +" , " 
//			+rs.getString("user_id_rec") +" , "+rs.getTimestamp("created_time") +" , "+rs.getString("text") 
//			+" , "+rs.getString("supplement") +" , "+rs.getString("visibility"));}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		RecordDAOImpl recordDAO = new RecordDAOImpl();
//		Record record = new Record("1", createdTime, "#spagetti", "https://rondo.com", Status.PRIVATE);
//		recordDAO.create(record);
//        int actual = recordDAO.getAll().size();
//        int expected = 3;
//        assertEquals(expected, actual);
//	}
    
}
