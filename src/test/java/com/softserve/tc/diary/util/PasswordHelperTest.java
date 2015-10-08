package com.softserve.tc.diary.util;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class PasswordHelperTest {
	
	@Test
	public void testLengthOfCrypt(){
		try {
			assertEquals(32, PasswordHelper.encrypt("admin").length());
			assertEquals(32, PasswordHelper.encrypt("user").length());
			assertEquals(32, PasswordHelper.encrypt("111111").length());
			assertEquals(32, PasswordHelper.encrypt("222222").length());
			assertEquals(32, PasswordHelper.encrypt("dibgfk").length());
			assertEquals(32, PasswordHelper.encrypt("UhTg56").length());
			
			assertEquals("b59c67bf196a4758191e42f76670ceba", PasswordHelper.encrypt("1111"));
			assertEquals("21232f297a57a5a743894a0e4a801fc3", PasswordHelper.encrypt("admin"));
			assertEquals("5950ae5e797f8e705c2c5bdb72e9a731", PasswordHelper.encrypt("Tggjhg21!"));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
