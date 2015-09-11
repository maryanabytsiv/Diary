package com.softserve.tc.diary.entity;

public class Tag {
	
	private String uuid;
	private String tag;
	
	public Tag(String uuid, String tag) {
		this.uuid = uuid;
		this.tag = tag;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tag [u_u_id= " + uuid + ", tag=" + tag + "]";
	}
	
	

}
