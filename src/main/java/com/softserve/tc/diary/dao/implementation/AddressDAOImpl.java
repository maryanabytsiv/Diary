package com.softserve.tc.diary.dao.implementation;

import java.util.List;

import com.softserve.tc.diary.dao.AddressDAO;
import com.softserve.tc.diary.entity.Address;
import com.softserve.tc.diary.entity.User;

public class AddressDAOImpl extends BaseDAOImpl<Address>implements AddressDAO {

    public List<User> getUsersByCountry(String country) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<User> getUsersByCity(String city) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Address> getByCity(String city) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMostCommonCountry() {
        // TODO Auto-generated method stub
        return null;
    }

    public int countAllByCountry(String country) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int countAllByCity(String city) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int countAllByStreet(String city) {
        // TODO Auto-generated method stub
        return 0;
    }

}
