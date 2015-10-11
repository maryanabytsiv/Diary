package com.softserve.tc.diary.util;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHelper {
    
    public static String encrypt(String password) throws NoSuchAlgorithmException {
    	return DigestUtils.md5Hex( password );
    }
    
}
