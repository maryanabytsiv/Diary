@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = LocalDateTime.class, 
                        value = LocalDateTimeAdapter.class)
})
package com.softserve.tc.diary.webservice;

import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;