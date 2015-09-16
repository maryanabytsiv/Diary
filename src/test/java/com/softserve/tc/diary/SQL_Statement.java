package com.softserve.tc.diary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.softserve.tc.log.Log;

/**
 * 
 * @author Mykola-
 *
 */
public class SQL_Statement {

	protected static Connection connection = null;
	protected static PreparedStatement ps = null;
	private static Logger logger = Log.init("SQL_Statement");

	public static void setUpBeforeClass() throws SQLException {

		BufferedReader br = null;
		String scriptSQL;
		String result = "";

		try {
			br = new BufferedReader(new FileReader("./PostgreSQL_DB/DiaryTest.sql"));
			while ((scriptSQL = br.readLine()) != null) {
				result += scriptSQL + "\n";
			}
		} catch (IOException e) {
			logger.error("can't read from file", e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				logger.error("can't close buffered reader", ex);
			}
		}

		try {
			connection = ConnectManager.getConnectionToTestDB();
			ps = connection.prepareStatement(result);
			ps.execute();
			ps.close();
		} catch (Exception e) {
			logger.error("connection failed", e);
		}
		if (connection.isClosed()) {
			logger.info("Connection closed");
		} else {
			logger.info("Connected to database!");
		}
	}

	public static void insertValue() throws SQLException {
		String insertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
				+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com',"
				+ "'kdfhgrr', 'MALE', '1992-02-02', null, '2');"
				+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com',"
				+ "'vfjukiuu', 'FEMALE', '1990-03-08', null, '1');"
				+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com',"
				+ "'flgkjhlkftjt', 'MALE', '1989-02-20', null, '2');"
				+ "insert into record_list values('1',null,'2015-02-23 00:00:00','#Hello my name is Bob. I am from #NewYork',"
				+ "'https://motivation/inUkraine/improveMySelf','PUBLIC');"
				+ "insert into record_list values('2',null,'2015-05-20 12:00:56','That was #nice day. #Halloween so cool',"
				+ "'http:/bigBoss/works/perfectly','PRIVATE');"
				+ "insert into record_list values('3',null,'2015-06-10 17:20:56','#HelloTeam, it is #nice to meet in NewYork',"
				+ "'http:/Lviv/theBest/Town/everSeen','PUBLIC');" + "insert into tag values('testkey1','#Hell');"
				+ "insert into tag values('testkey2','#Hello');" + "insert into tag values('testkey3','#HelloWorld');"
				+ "insert into tag values('testkey4','#HellGuy');" + "insert into tag values('testkey5','#nice');"
				+ "insert into tag values('testkey6','#Halloween');"
				+ "insert into tag values('testkey7','#HelloTeam');" + "insert into tag values('testkey8','#NewYork');"
				+ "insert into tag values('testkey9','#HelpMe');" + "insert into tag_record values('rt1',1,'testkey2');"
				+ '\n' + "insert into tag_record values('rt2',1,'testkey8');" + '\n'
				+ "insert into tag_record values('rt3',2,'testkey5');" + '\n'
				+ "insert into tag_record values('rt4',2,'testkey6');" + '\n'
				+ "insert into tag_record values('rt5',3,'testkey7');" + '\n'
				+ "insert into tag_record values('rt6',3,'testkey5');" + '\n';
		try {
			ps = connection.prepareStatement(insertData);
			ps.execute();
		} catch (SQLException e) {
			logger.error("insert failed", e);
		}
	}

	public static void deleteAllFromTable() throws SQLException {
		String deleteData = "delete from tag_record;" + "delete from record_list;" + "delete from user_card;"
				+ "delete from address;" + "delete from role;" + "delete from tag;";
		try {
			ps = connection.prepareStatement(deleteData);
			ps.execute();
		} catch (SQLException e) {
			logger.error("delete failed", e);
		}
	}

	public static void DropTableIfExists() throws SQLException {
		try {
			ps = connection.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
					+ "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;" + "DROP TABLE IF EXISTS role;"
					+ "DROP TABLE IF EXISTS tag;");
		} catch (Exception e) {
			logger.error("drop table failed", e);
		} finally {
			ps.execute();
			ps.close();
			connection.close();
			logger.info(connection.isClosed() ? "Connections are closed." : "Connections are not closed.");
		}
	}
}
