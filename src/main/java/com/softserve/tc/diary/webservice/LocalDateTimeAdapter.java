package com.softserve.tc.diary.webservice;

import org.joda.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
    	return LocalDateTime.parse(s);
    }
    @Override
    public String marshal(LocalDateTime dateTime) throws Exception {
    	DateTimeFormatter formatter =
    		    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
    		                     .withLocale( Locale.UK )
    		                     .withZone( ZoneId.systemDefault() );
    	
    	return dateTime.toString();
    }   
}
