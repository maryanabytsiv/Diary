package com.softserve.tc.diary.dao;

import java.util.List;

import com.softserve.tc.diary.entity.Role;

public interface RoleDAO extends BaseDAO<Role> {

	List<Role> findByRole(String role);

	// Add 2 methods
}
