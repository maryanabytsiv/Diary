package com.softserve.tc.diary.entity;

public class Tag {
	
	private int u_u_id;
	private String tag;
	
	public Tag(int u_u_id, String tag) {
		this.u_u_id = u_u_id;
		this.tag = tag;
	}
	
	public int getU_u_id() {
		return u_u_id;
	}
	
	public void setU_u_id(int u_u_id) {
		this.u_u_id = u_u_id;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tag [u_u_id=" + u_u_id + ", tag=" + tag + "]";
	}
	
	
	
	

}
