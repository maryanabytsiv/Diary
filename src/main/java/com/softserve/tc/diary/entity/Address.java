package com.softserve.tc.diary.entity;

public class Address {
    
    private String uuid;
    private String country;
    private String city;
    private String street;
    private String buildNumber;
    
    public Address() {
    
    }
    
    public Address(String country, String city, String street,
            String buildNumber) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.buildNumber = buildNumber;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getBuildNumber() {
        return buildNumber;
    }
    
    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }
    
    @Override
    public String toString() {
        return country + " " + city + " " + street + " " + buildNumber;
    }
    
}
