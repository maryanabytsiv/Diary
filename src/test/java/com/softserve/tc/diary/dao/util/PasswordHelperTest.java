package com.softserve.tc.diary.dao.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordHelperTest {
	
	@Test
	public void testLengthOfCrypt(){
		assertEquals(32, PasswordHelper.encrypt("admin").length());
		assertEquals(32, PasswordHelper.encrypt("user").length());
		assertEquals(32, PasswordHelper.encrypt("111111").length());
		assertEquals(32, PasswordHelper.encrypt("222222").length());
		assertEquals(32, PasswordHelper.encrypt("dibgfk").length());
		assertEquals(32, PasswordHelper.encrypt("UhTg56").length());
		System.out.println(PasswordHelper.encrypt("UhTg56").toString());
		
		
	}

}
