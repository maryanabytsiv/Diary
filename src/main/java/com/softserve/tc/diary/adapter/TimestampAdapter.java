package com.softserve.tc.diary.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp>{

    @Override
    public String marshal(Timestamp v) throws Exception {
    	System.out.println(v);
        return v.toString();
    }

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(v);
        return new Timestamp(parsedDate.getTime());

    }

}
