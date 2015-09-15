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
	
    protected static Connection connection = null;
    protected static PreparedStatement ps = null;
    
	public static void setUpBeforeClass() throws SQLException {

		BufferedReader br = null;
		String scriptSQL;
		String result = "";
//		if(connection == null || connection.isClose()){
//			return;
//		}
		
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
		if (connection.isClosed()) {
			System.out.println("close");
		} else {
			System.out.println("Ok");
		}
	} 
	
	public static void insertValue() throws SQLException{
		String insertData = "insert into role values('1','Admin') ;" + " insert into role values('2','User') ;"
				+ "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
				+ "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
				+ "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
			+ "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com',"
														+"'kdfhgrr', 'MALE', '1992-02-02', null, '2');"
			+ "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com',"
														+"'vfjukiuu', 'FEMALE', '1990-03-08', null, '1');"
			+ "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com',"
														+"'flgkjhlkftjt', 'MALE', '1989-02-20', null, '2');"
	+ "insert into record_list values('1',null,'2015-02-23 00:00:00','#Hello my name is Bod. I am from #NewYork',"
										+"'https://motivation/inUkraine/improveMySelf','PUBLIC');"
	+ "insert into record_list values('2',null,'2015-05-20 12:00:56','That was #nice day. #Halloween so cool',"
										+"'http:/bigBoss/works/perfectly','PRIVATE');"
	+ "insert into record_list values('3',null,'2015-06-10 17:20:56','#HelloTeam, it is #nice to meet in NewYork',"
										+"'http:/Lviv/theBest/Town/everSeen','PUBLIC');"
				+ "insert into tag values('testkey1','#Hell');"
				+ "insert into tag values('testkey2','#Hello');"
				+ "insert into tag values('testkey3','#HelloWorld');"
				+ "insert into tag values('testkey4','#HellGuy');"
				+ "insert into tag values('testkey5','#nice');"
				+ "insert into tag values('testkey6','#Halloween');"
				+ "insert into tag values('testkey7','#HelloTeam');"
				+ "insert into tag values('testkey8','#NewYork');"
				+ "insert into tag values('testkey9','#HelpMe');"
						+ "insert into tag_record values('rt1',1,'testkey2');" + '\n'
						+ "insert into tag_record values('rt2',1,'testkey8');" + '\n'
						+ "insert into tag_record values('rt3',2,'testkey5');" + '\n'
						+ "insert into tag_record values('rt4',2,'testkey6');" + '\n'
						+ "insert into tag_record values('rt5',3,'testkey7');" + '\n'
						+ "insert into tag_record values('rt6',3,'testkey5');" + '\n';
		try {
			ps = connection.prepareStatement(insertData);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			}	
		}

	public static void deleteAllFromTable() throws SQLException{
		String deleteData="delete from tag_record;"+"delete from record_list;"+"delete from user_card;"+"delete from address;"+"delete from role;"+"delete from tag;";
//		String deleteData="delete from tag_record;"+"delete from record_list;"+"delete from address;"+"delete from role;"+"delete from tag;"+"delete from user_card;";
				try {
					ps = connection.prepareStatement(deleteData);
					ps.execute();
				} catch (SQLException e) {
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
	}
