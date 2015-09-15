package com.softserve.tc.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {

    public static Logger init(String className) {
        final Logger logger = Logger.getLogger(className);
        PropertyConfigurator.configure("log4j.properties");
        return logger;
    }
}
