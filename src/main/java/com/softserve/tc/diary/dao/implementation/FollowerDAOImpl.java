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
import com.softserve.tc.diary.connectionmanager.DataBase;
import com.softserve.tc.diary.dao.BaseDAO;
import com.softserve.tc.diary.dao.FollowerDAO;
import com.softserve.tc.diary.entity.Follower;
import com.softserve.tc.diary.entity.User;
import com.softserve.tc.diary.log.Log;
import com.softserve.tc.diary.util.Constant;

public class FollowerDAOImpl implements FollowerDAO, BaseDAO<Follower> {
    
    private PreparedStatement ps;
    private Logger logger = Log.init(this.getClass().getName());
    private UserDAOImpl userDAO = UserDAOImpl
            .getInstance(ConnectionManager.getInstance(DataBase.REALDB));
    private static ConnectionManager connection = null;
    
    private static FollowerDAOImpl followerDAO = null;
    
    private FollowerDAOImpl() {
    }
    
    public static FollowerDAOImpl getInstance(ConnectionManager connect) {
        
        if (followerDAO == null) {
            followerDAO = new FollowerDAOImpl();
            connection = connect;
        }
        return followerDAO;
        
    }
    
    @Override
    public String create(Follower object) {
        logger.debug("Creating follower");
        String uuid = UUID.randomUUID().toString();
        try (Connection conn = connection.getConnection()) {
            ps = conn
                    .prepareStatement(
                            "insert into followers(uuid, follower_uuid, user_uuid) values(?,?,?)");
            ps.setString(1, uuid);
            ps.setString(2, object.getFollower().getUuid());
            ps.setString(3, object.getUser().getUuid());
            ps.execute();
            ps.close();
            logger.debug("Follower created");
        } catch (SQLException e) {
            logger.error("Creating follower failed", e);
        }
        return uuid;
    }
    
    @Override
    public Follower readByKey(String id) {
        Follower follower = null;
        logger.debug("Reading by key");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select * from followers where uuid=?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                follower = new Follower(
                        userDAO.readByKey(
                                rs.getString(Constant.Follower.FOLLOWER)),
                        userDAO.readByKey(
                                rs.getString(Constant.Follower.USER)));
                follower.setUuid(rs.getString(Constant.Follower.UUID));
                follower.setNewUserRecord(Boolean.valueOf(rs.getString(Constant.Follower.NEW_USER_RECORD)));
            }
            logger.debug("ReadByKey done");
        } catch (SQLException e) {
            logger.error("Can't select from Follower", e);
        }
        return follower;
    }
    
    @Override
    public void update(Follower object) {
        
        logger.debug("Updating follower");
        try (Connection conn = connection.getConnection()) {
            
            try {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(
                        "update followers set follower_uuid = ?, user_uuid = ?, user_has_new_record = ? where uuid = ?");
                ps.setString(1, object.getFollower().getUuid());
                ps.setString(2, object.getUser().getUuid());
                ps.setString(3, String.valueOf(object.isNewUserRecord()));
                ps.setString(4, object.getUuid());
                ps.execute();
                ps.close();
                logger.debug("Follower updated");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            logger.error("Can't update follwer", e);
        }
        
    }
    
    @Override
    public void delete(Follower object) {
        try (Connection conn = connection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                logger.debug("Deleting follower");
                if (object.getUuid() != null) {
                    ps = conn.prepareStatement(
                            "delete from followers where uuid = ?");
                    ps.setString(1, object.getUuid());
                    ps.execute();
                } else {
                    ps = conn.prepareStatement(
                            "delete from followers where follower_uuid = ? and user_uuid = ?");
                    ps.setString(1, object.getFollower().getUuid());
                    ps.setString(2, object.getUser().getUuid());
                    ps.execute();
                }
                logger.debug("follower deleted");
                conn.commit();
            } catch (SQLException e) {
                logger.error("Error. Rollback changes", e);
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Can't delete follower", e);
        }
        
    }
    
    @Override
    public List<Follower> getAll() {
        List<Follower> list = new ArrayList<Follower>();
        logger.debug("Get all follower records");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select * from followers;");
            ResultSet rs = ps.executeQuery();
            Follower follower = null;
            while (rs.next()) {
                follower = new Follower(
                        userDAO.readByKey(
                                rs.getString(Constant.Follower.FOLLOWER)),
                        userDAO.readByKey(
                                rs.getString(Constant.Follower.USER)));
                follower.setUuid(rs.getString(Constant.Follower.UUID));
                follower.setNewUserRecord(Boolean.valueOf(rs.getString(Constant.Follower.NEW_USER_RECORD)));
                list.add(follower);
                
            }
        } catch (SQLException e) {
            logger.error("Can't getAll followers", e);
        }
        return list;
    }

    @Override
    public List<User> getAllUserFollowers(String userUuid) {
        List<User> list = new ArrayList<User>();
        logger.debug("Get followers");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select follower_uuid from followers where user_uuid = ?;");
            ps.setString(1, userUuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(userDAO.readByKey(rs.getString(Constant.Follower.FOLLOWER)));
                
            }
        } catch (SQLException e) {
            logger.error("Can't get followers", e);
        }
        return list;
    }

    @Override
    public List<User> getAllFollowedUsers(String followerUuid) {
        List<User> list = new ArrayList<User>();
        logger.debug("Get user");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select user_uuid from followers where follower_uuid = ?;");
            ps.setString(1, followerUuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(userDAO.readByKey(rs.getString(Constant.Follower.USER)));
                
            }
        } catch (SQLException e) {
            logger.error("Can't get users", e);
        }
        return list;
    }

    @Override
    public void markUserWithNewRecord(String userUuid) {
        logger.debug("Mark user");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("update followers set  user_has_new_record = ? where user_uuid = ?");
            ps.setString(1, "true");
            ps.setString(2, userUuid);
            ps.execute();
        } catch (SQLException e) {
            logger.error("Can't mark user", e);
        }
    }

    @Override
    public void markAsViwed(String followerUuid) {
        logger.debug("Mark user as visited");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("update followers set  user_has_new_record = ? where follower_uuid = ?");
            ps.setString(1, "false");
            ps.setString(2, followerUuid);
            ps.execute();
        } catch (SQLException e) {
            logger.error("Can't mark user", e);
        }
    }

    @Override
    public List<User> getAllReviewedUsers(String followerUuid) {
        List<User> list = new ArrayList<User>();
        logger.debug("Get all reviewed users");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select user_uuid from followers where user_has_new_record = 'false' and follower_uuid = ?;");
            ps.setString(1, followerUuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(userDAO.readByKey(rs.getString(Constant.Follower.USER)));
                
            }
        } catch (SQLException e) {
            logger.error("Can't get users", e);
        }
        return list;
    }

    @Override
    public List<User> getAllNotReviewedUsers(String followerUuid) {
        List<User> list = new ArrayList<User>();
        logger.debug("Get all not revieved users");
        try (Connection conn = connection.getConnection()) {
            ps = conn.prepareStatement("select user_uuid from followers where user_has_new_record = 'true' and follower_uuid = ?;");
            ps.setString(1, followerUuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(userDAO.readByKey(rs.getString(Constant.Follower.USER)));
                
            }
        } catch (SQLException e) {
            logger.error("Can't get users", e);
        }
        return list;
    }
    
}
