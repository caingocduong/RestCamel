package com.java.repositories;

import java.util.List;

import com.java.models.User;

public interface UserRepository {
	void createPerson(User person);
	List<User> retrieveAll();
	User findById(long id);
	void update(User person);
	void delete(long id);
}
