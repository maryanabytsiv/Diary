package com.softserve.tc.diary.connectionmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.softserve.tc.diary.log.Log;

/**
 * 
 * @author Mykola-
 *         
 */
public class DBCreationManagerTest {
    
    private static PreparedStatement ps = null;
    private static Logger logger = Log.init("SQL_Statement");
    private static final ConnectionManager conn =
            DBConnectionManager.getInstance(false);
            
    public static void setUpBeforeClass() throws SQLException {
        
        String scriptSQL;
        String result = "";
        
        try (BufferedReader br = new BufferedReader(
                new FileReader("./PostgreSQL_DB/DiaryTest.sql"))) {
            while ((scriptSQL = br.readLine()) != null) {
                result += scriptSQL + "\n";
            }
        } catch (IOException e) {
            logger.error("can't read from file", e);
        }
        
        try (Connection connection = conn.getConnection()) {
            
            connection.setAutoCommit(false);
            try {
                ps = connection.prepareStatement(result);
                ps.execute();
                ps.close();
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
                connection.setAutoCommit(true);
            }
            connection.setAutoCommit(true);
        } catch (Exception e) {
            logger.error("connection failed", e);
        }
        
    }
    
    public static void insertValue() throws SQLException {
        
        String insertData =
                "insert into address values('1','Ukraine', 'Lviv', 'centre', 3) ;"
                        + "insert into address values('2','USA', 'NC', 'timesquare', 5) ;"
                        + "insert into address values('3','Poland', 'Warshav', 'Bog', 55);"
                        + "insert into user_card values('1','BigBunny', 'Oleg', 'Pavliv', '2', 'hgdf@gmail.com',"
                        + "'kdfhgrr', 'MALE', '1992-02-02', null, 'USER', null);"
                        + "insert into user_card values('2','Sonic', 'Ira', 'Dub', '1', 'dfhfght@gmail.com',"
                        + "'vfjukiuu', 'FEMALE', '1990-03-08', null, 'ADMIN', null);"
                        + "insert into user_card values('3','TreeTree', 'Sergey', 'Gontar', '3', 'jhfcjfdf@gmail.com',"
                        + "'flgkjhlkftjt', 'MALE', '1989-02-20', null, 'USER', null);"
                        + "insert into record_list values('1','1','2015-02-23 00:00:00', 'bob' ,'#Hello my name is Bob. I am from #NewYork', "
                        + "'https://motivation/inUkraine/improveMySelf','PUBLIC');"
                        + "insert into record_list values('2','1','2015-05-20 12:00:56', 'Halloween', 'That was #nice day. #Halloween so cool', "
                        + "'http:/bigBoss/works/perfectly','PRIVATE');"
                        + "insert into record_list values('3','2','2015-06-10 17:20:56', 'Team', '#HelloTeam, it is #nice to meet in NewYork', "
                        + "'http:/Lviv/theBest/Town/everSeen','PUBLIC');"
                        + "insert into tag values('testkey1','#Hell');"
                        + "insert into tag values('testkey2','#Hello');"
                        + "insert into tag values('testkey3','#HelloWorld');"
                        + "insert into tag values('testkey4','#HellGuy');"
                        + "insert into tag values('testkey5','#nice');"
                        + "insert into tag values('testkey6','#Halloween');"
                        + "insert into tag values('testkey7','#HelloTeam');"
                        + "insert into tag values('testkey8','#NewYork');"
                        + "insert into tag values('testkey9','#HelpMe');"
                        + "insert into tag_record values('rt1',1,'testkey2');"
                        + '\n'
                        + "insert into tag_record values('rt2',1,'testkey8');"
                        + '\n'
                        + "insert into tag_record values('rt3',2,'testkey5');"
                        + '\n'
                        + "insert into tag_record values('rt4',2,'testkey6');"
                        + '\n'
                        + "insert into tag_record values('rt5',3,'testkey7');"
                        + '\n'
                        + "insert into tag_record values('rt6',3,'testkey5');"
                        + '\n';
        try (Connection connection = conn.getConnection()) {
            ps = connection.prepareStatement(insertData);
            ps.execute();
        } catch (SQLException e) {
            logger.error("insert failed", e);
        }
    }
    
    public static void deleteAllFromTable() throws SQLException {
        
        String deleteData = "delete from tag_record;"
                + "delete from record_list;" + "delete from user_card;"
                + "delete from address;"
                + "delete from tag;";
        try (Connection connection = conn.getConnection()) {
            connection.setAutoCommit(false);
            try {
                ps = connection.prepareStatement(deleteData);
                ps.execute();
                ps.close();
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
                connection.setAutoCommit(true);
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("delete failed", e);
        }
    }
    
    public static void DropTableIfExists() throws SQLException {
        
        String dropTable = "DROP TABLE IF EXISTS tag_record;"
                + "DROP TABLE IF EXISTS record_list;"
                + "DROP TABLE IF EXISTS user_card;"
                + "DROP TABLE IF EXISTS address;"
                + "DROP TABLE IF EXISTS tag;";
        try (Connection connection = conn.getConnection()) {
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(dropTable);
                ps.execute();
                ps.close();
                connection.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                connection.rollback();
                connection.setAutoCommit(true);
            }
            connection.setAutoCommit(true);
            
        } catch (Exception e) {
            logger.error("drop table failed", e);
        }
    }
}
