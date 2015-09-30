package com.softserve.tc.diary.log;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
    
    public static Logger init(String className) {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	InputStream is = classloader.getResourceAsStream("log4j.properties");
        final Logger logger = Logger.getLogger(className);
        PropertyConfigurator.configure(is);
        return logger;
    }
}
