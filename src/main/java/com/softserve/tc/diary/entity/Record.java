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
    
    private String uuid;
    private String userId;
    private Timestamp createdTime;
    private String title;
    private String text;
    private String supplement;
    private Status visibility;
    
    public Record() {
    
    }

    public Record(String userName, String title,
            String text, String supplement, Status visibility) {
        this.userId = userName;
        this.title = title;
        this.text = text;
        this.supplement = supplement;
        this.visibility = visibility;
    }
    
    public Record(String userName, Timestamp createdTime, String title,
    		String text, String supplement, Status visibility) {
        this.userId = userName;
        this.createdTime = createdTime;
        this.title = title;
        this.text = text;
        this.supplement = supplement;
        this.visibility = visibility;
    }
    
    public Record(String uuid, String userName, Timestamp createdTime,
    		 String title,  String text, String supplement, Status visibility) {
        this.uuid = uuid;
        this.userId = userName;
        this.createdTime = createdTime;
        this.title = title;
        this.text = text;
        this.supplement = supplement;
        this.visibility = visibility;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getUserId() {
        return userId;
    }
    
    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
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
    
	public String getVisibility() {
        if (visibility == Status.PRIVATE) {
            return "PRIVATE";
        } else if (visibility == Status.PUBLIC) {
            return "PUBLIC";
        } else
            return null;
    }
	
	
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return "Record [uuid=" + uuid + ", user id=" + userId + ", created time=" + createdTime + ", title="
				+ title + ", text=" + text + ", supplement=" + supplement + ", visibility=" + visibility + "]";
	}


    
    
}
