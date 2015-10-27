package com.softserve.tc.diary.entity;

public class Follower {
    private String uuid;
    private User follower;
    private User user;
    private boolean newUserRecord;
    
    public Follower() {
    }

    public Follower(User follower, User user) {
        super();
        this.follower = follower;
        this.user = user;
    }
    
    public Follower(String uuid, User follower, User user) {
        super();
        this.uuid = uuid;
        this.follower = follower;
        this.user = user;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public User getFollower() {
        return follower;
    }
    
    public void setFollower(User follower) {
        this.follower = follower;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public boolean isNewUserRecord() {
        return newUserRecord;
    }
    
    public void setNewUserRecord(boolean newUserRecord) {
        this.newUserRecord = newUserRecord;
    }
    
    @Override
    public String toString() {
        return "Follower [uuid=" + uuid + ", follower=" + follower + ",\n user="
                + user + ", newUserRecord=" + newUserRecord + "]\n";
    }
    
}
