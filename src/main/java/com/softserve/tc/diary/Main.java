package com.softserve.tc.diary;

import com.softserve.tc.diary.dao.implementation.AddressDAOImpl;
import com.softserve.tc.diary.dao.util.PasswordHelper;
import com.softserve.tc.diary.entity.Address;

public class Main {
    
    public static void main(String[] args) {
    System.out.println(PasswordHelper.encrypt("kdfhgrr"));
    System.out.println(PasswordHelper.encrypt("kdfhgrr"));

    }
}
