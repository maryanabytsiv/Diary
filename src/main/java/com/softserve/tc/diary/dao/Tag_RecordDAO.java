package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.Tag_Record;

public interface Tag_RecordDAO extends BaseDAO<Tag_Record>{

	//BaseDAO
	void create(Tag_Record object);

	Tag_Record readByKey(int id);

	void update(Tag_Record object);

	void delete(Tag_Record object);

	List<Tag_Record> getAll();
	
	//Andrii Shupta
	
	Tag_Record getById(int id);
	Tag_Record getByRecord(int recordId);
	Tag_Record getByTag(int tagId);
	
}
