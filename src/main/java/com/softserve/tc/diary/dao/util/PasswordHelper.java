package com.softserve.tc.diary.dao.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHelper {
    
    public static String encrypt(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
            	String hex = Integer.toHexString(0xff & digested[i]);
            	if(hex.length()==1)
            		sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }
    
}
