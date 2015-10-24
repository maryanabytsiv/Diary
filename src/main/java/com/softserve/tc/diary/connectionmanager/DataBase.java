package com.softserve.tc.diary.connectionmanager;

public enum DataBase implements ConnectionPath{
	
	REALDB("realDB.properties");
	
	private String pathToDB;
	
	DataBase(String path) {
        this.pathToDB = path;
    }
    
	@Override
    public String getPath() {
        return this.pathToDB;
    }

}
