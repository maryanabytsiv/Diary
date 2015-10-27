package com.softserve.tc.diary.connectionmanager;

public enum DataBase implements PropertyFileNameProvider{
	
	REALDB("realDB.properties"), CLOUDDB("cloudDB.properties");
	
	private String propertyFileName;
	
	DataBase(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }
    
	@Override
    public String getName() {
        return this.propertyFileName;
    }

}
