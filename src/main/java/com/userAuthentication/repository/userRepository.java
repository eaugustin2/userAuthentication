package com.userAuthentication.repository;

import com.userAuthentication.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends CrudRepository <User,Integer>{ 
	
	//This interface will automatically generate CRUD methods since it extends CrudRepository, I can still make custom methods below in the interface
	
	//need a method to find user by email to make sure they haven't already created an account
	User findByEmail(String email);
}
