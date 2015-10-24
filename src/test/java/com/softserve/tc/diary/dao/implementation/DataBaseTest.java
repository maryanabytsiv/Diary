package com.softserve.tc.diary.dao.implementation;

import com.softserve.tc.diary.connectionmanager.PropertyFileNameProvider;

enum DataBaseTest implements PropertyFileNameProvider{
	
	TESTDB("testDB.properties");

private String propertyFileName;
	
	DataBaseTest(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }
    
	@Override
    public String getName() {
        return this.propertyFileName;
    }


}
