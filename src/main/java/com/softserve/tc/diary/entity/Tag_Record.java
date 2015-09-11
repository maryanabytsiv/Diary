package com.softserve.tc.diary.entity;

public class Tag_Record {
	
	
	private int u_u_id;
	private int record_tag;
	private int tag_uuid;
	
	public Tag_Record(){
		
	}
	
	public Tag_Record(int record_tag, int tag_uuid) {
		this.record_tag = record_tag;
		this.tag_uuid = tag_uuid;
	}
	
	
	
	public int getU_u_id() {
		return u_u_id;
	}

	public void setU_u_id(int u_u_id) {
		this.u_u_id = u_u_id;
	}

	public int getRecord_tag() {
		return record_tag;
	}

	public void setRecord_tag(int record_tag) {
		this.record_tag = record_tag;
	}

	public int getTag_uuid() {
		return tag_uuid;
	}

	public void setTag_uuid(int tag_uuid) {
		this.tag_uuid = tag_uuid;
	}

	@Override
	public String toString() {
		return "Tag_Record [u_u_id=" + u_u_id + ", record_tag=" + record_tag + ", tag_uuid=" + tag_uuid + "]";
	}

	
	
}
