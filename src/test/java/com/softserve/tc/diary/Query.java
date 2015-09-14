package com.softserve.tc.diary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author Mykola-
 *
 */
public class Query {
	
    protected static Connection connection;
    protected static PreparedStatement ps;
    
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
            connection = ConnectManager.getConnectionToTestDB();
			ps = connection.prepareStatement(result);
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public static void DropTableIfExists() throws SQLException {
		try{
		 ps = connection.prepareStatement("DROP TABLE IF EXISTS tag_record;" + "DROP TABLE IF EXISTS record_list;"
		 + "DROP TABLE IF EXISTS user_card;" + "DROP TABLE IF EXISTS address;"
		 + "DROP TABLE IF EXISTS role;"
		 + "DROP TABLE IF EXISTS tag;");
		 }catch (Exception e) {
			 e.printStackTrace();
			 }finally{
				 ps.execute();
				 ps.close();
				 connection.close();
				 }
		}
	
	public static void insertValue() throws SQLException{
		String isertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
				+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com', 'kdfhgrr', 'MALE', '1992-02-02', null, '2');"
				+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com', 'vfjukiuu', 'FEMALE', '1990-03-08', null, '1');"
				+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com', 'flgkjhlkftjt', 'MALE', '1989-02-20', null, '2');"
				+ "insert into record_list values('1','1','2015-02-23 00:00:00','skjdhugfkdxufgesiurkgtiudshkfjghkdf',null,'PUBLIC');"
				+ "insert into record_list values('2','3','2015-05-20 12:00:56','skjdhugfkdxufge',null,'PRIVATE');"
				+ "insert into record_list values('3','1','2015-06-10 17:20:56','fkjb5kj4g5khg4555xufge',null,'PUBLIC');"
				+ "insert into tag values('1','#cars');" + "insert into tag values('3','#family');"
				+ "insert into tag values('2','#Love');" + "insert into tag values('4','#murderrrrrr');"
				+ "insert into tag_record values('1','1', '2');"
				+ "insert into tag_record values('2','3', '1');"
				+ "insert into tag_record values('3','2', '3');";
		try {
			ps = connection.prepareStatement(isertData);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			}	
		}

	public static void deleteAllFromTable() throws SQLException{
		String deleteData="delete from tag_record;"+"delete from record_list;"+"delete from user_card;"+"delete from address;"+"delete from role;"+"delete from tag;";
				try {
					ps = connection.prepareStatement(deleteData);
					ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
					}
				}
	}
