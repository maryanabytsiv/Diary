package com.softserve.tc.diary.adapter;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp>{

    @Override
    public String marshal(Timestamp v) throws Exception {
        return v.toString();
    }

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return new Timestamp(Long.parseLong(v));
    }

}
