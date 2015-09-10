package com.softserve.tc.diary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {
	public static final String URL = "jdbc:postgresql://localhost:5432/DiaryTest";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	private static Connection conn;

	public static void main(String[] args) {

		BufferedReader br = null;
		String result = "";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("G:\\Projects\\Diary\\PostgreSQL_DB\\DiaryTest.sql"));
			while ((sCurrentLine = br.readLine()) != null) {
				result += sCurrentLine+"\n";
			}
			System.out.println(result);
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
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement ps = conn.prepareStatement(result);
			ps.execute();
			// ResultSet myRs = ps.executeQuery("select * from city");
			// while (myRs.next()){
			// System.out.println(myRs.getString(1) + " , "
			// + myRs.getString("country_id") + " , " +
			// myRs.getString("city_name"));}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
