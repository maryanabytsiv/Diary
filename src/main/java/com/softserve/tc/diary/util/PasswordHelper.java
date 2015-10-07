package com.softserve.tc.diary.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHelper {
    
    public static String encrypt(String password) {
    	return DigestUtils.md5Hex( password );
    }
    
}
