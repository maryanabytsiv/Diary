package com.softserve.tc.diary.entity;

public class Tag {
	
	private String uuid;
	private String tag;
	
	public Tag(String tag) {
		this.tag = tag;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getTagMessage() {
		return tag;
	}
	
	public void setTagMessage(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tag [u_u_id= " + uuid + ", tag=" + tag + "]";
	}
	
	

}
