package com.softserve.tc.diary.entity;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.softserve.tc.diary.adapter.TimestampAdapter;

/**
 * 
 * @author Mykola-
 *        
 */

public class Record {
    
    private String id_rec;
    private String user_name;
    private Timestamp created_time;
    private String title;
    private String text;
    private String supplement;
    private Status visibility;
    
    public Record() {
    
    }
    
    public Record(String user_name, Timestamp created_time, String title,
    		String text, String supplement, Status visibility) {
        this.user_name = user_name;
        this.created_time = created_time;
        this.title = title;
        this.text = text;
        this.supplement = supplement;
        this.visibility = visibility;
    }
    
    public Record(String id_rec, String user_name, Timestamp created_time,
    		 String title, String text, String supplement, Status visibility) {
        this.id_rec = id_rec;
        this.user_name = user_name;
        this.created_time = created_time;
        this.title = title;
        this.text = text;
        this.supplement = supplement;
        this.visibility = visibility;
    }
    
    public String getId_rec() {
        return id_rec;
    }
    
    public void setId_rec(String id_rec) {
        this.id_rec = id_rec;
    }
    
    public String getUser_name() {
        return user_name;
    }
    
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getCreated_time() {
        return created_time;
    }
    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getSupplement() {
        return supplement;
    }
    
    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }
    
    
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVisibility() {
        if (visibility == Status.PRIVATE) {
            return "PRIVATE";
        } else if (visibility == Status.PUBLIC) {
            return "PUBLIC";
        } else
            return null;
    }
    
    public void setVisibility(String visibility) {
        if (visibility.equals("PRIVATE")) {
            this.visibility = Status.PRIVATE;
        } else if (visibility.equals("PUBLIC")) {
            this.visibility = Status.PUBLIC;
        } else
            this.visibility = null;
    }

	@Override
	public String toString() {
		return "Record [id_rec=" + id_rec + ", user_name=" + user_name + 
				", created_time=" + created_time + ", title="
				+ title + ", text=" + text + ", supplement="
				+ supplement + ", visibility=" + visibility + "]";
	}
    
    
    
}
