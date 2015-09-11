package com.softserve.tc.diary.dao;

import java.util.List;

public interface BaseDAO<T> {
	
	void create(T object);
	T readByKey(String id);
	void update(T object);
	void delete (T object);
	List<T> getAll();
}
