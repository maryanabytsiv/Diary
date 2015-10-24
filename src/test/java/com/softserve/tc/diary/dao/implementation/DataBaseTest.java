package com.softserve.tc.diary.dao.implementation;

import com.softserve.tc.diary.connectionmanager.ConnectionPath;

enum DataBaseTest implements ConnectionPath{
	
	TESTDB("testDB.properties");

private String pathToDB;
	
	DataBaseTest(String path) {
        this.pathToDB = path;
    }
    
	@Override
    public String getPath() {
        return this.pathToDB;
    }


}
