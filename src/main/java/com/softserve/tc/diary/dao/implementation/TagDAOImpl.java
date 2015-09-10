package com.softserve.tc.diary.dao.implementation;

import java.util.List;

import com.softserve.tc.diary.dao.TagDAO;
import com.softserve.tc.diary.entity.Record;
import com.softserve.tc.diary.entity.Tag;

public class TagDAOImpl extends BaseDAOImpl<Tag> implements TagDAO {

	@Override
	public void create(Tag object) {
		// TODO Auto-generated method stub
		super.create(object);
	}

	@Override
	public Tag readByKey(int id) {
		// TODO Auto-generated method stub
		return super.readByKey(id);
	}

	@Override
	public void update(Tag object) {
		// TODO Auto-generated method stub
		super.update(object);
	}

	@Override
	public void delete(Tag object) {
		// TODO Auto-generated method stub
		super.delete(object);
	}

	@Override
	public List<Tag> getAll() {
		// TODO Auto-generated method stub
		return super.getAll();
	}

	public int getTagIdByTagMessage(String TagMessage) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Tag> getListTagsByPrefix(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tag> getListTagsBySuffix(String suffix) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getListRecordsByTag() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Record> getListRecordsByListOfTags(List<Tag> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
