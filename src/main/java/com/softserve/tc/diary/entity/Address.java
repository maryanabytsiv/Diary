package com.softserve.tc.diary.entity;

public class Address {

    private int id;
    private String country;
    private String city;
    private String street;
    private int build_number;

    public Address() {

    }

	
    public Address(int id, String country, String city, String street, int build_number) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.build_number = build_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getBuild_number() {
        return build_number;
    }

    public void setBuild_number(int build_number) {
        this.build_number = build_number;
    }

    @Override
    public String toString() {
        return "Address [id=" + id + ", country=" + country + ", city=" + city + ", street=" + street
                + ", build_number=" + build_number + "]";
    }

}