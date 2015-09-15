package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.Address;
//import com.softserve.tc.diary.entity.User;

public interface AddressDAO extends BaseDAO<Address> {

//    List<User> getUsersByCountry(String country);
//    
//    List<User> getUsersByCity(String city);
//    
//    List<Address> getByCity(String city);
//
//    String getMostCommonCountry();
//    
//    int countAllByCountry(String country);
//    
//    int countAllByCity(String city);
//
//    int countAllByStreet(String city);

    List<Address> getAll();
}
