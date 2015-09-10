package com.softserve.tc.diary.dao.implementation;

import java.util.List;

import com.softserve.tc.diary.dao.BaseDAO;

public abstract class BaseDAOImpl<T> implements BaseDAO<T>{

	public abstract void create(T object);

	public abstract T readByKey(int id) ;

	public abstract void update(T object) ;

	public abstract void delete(T object) ;

	public abstract List<T> getAll() ;
}