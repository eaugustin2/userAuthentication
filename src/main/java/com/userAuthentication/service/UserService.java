package com.userAuthentication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.userAuthentication.model.User;
import com.userAuthentication.repository.userRepository;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder encoder; //this interface is used after it was declared in bean configuration
	
	@Autowired
	userRepository userRepo;
	
	/*
	 * Method to create a new user if the user does not already exist within repository
	 */
	public void createUser (User userObj) {
		Optional<User> isUser = userRepo.findById(userObj.getId());
		
		if(!isUser.isPresent()) {
			
			User newUser = new User();
			newUser.setFirstName(userObj.getFirstName());
			newUser.setLastName(userObj.getLastName());
			newUser.setEmail(userObj.getEmail());
			String hashedPassword = encoder.encode(userObj.getPassword());
			newUser.setPassword(hashedPassword);
			userRepo.save(newUser);
			System.out.println("New User has been created...");
			
		}
		else {
			System.out.println("This user already has an account...");
		}
	}
	
	/*
	 * Method to update a users password
	 */
	public void updatePassword(User userObj, String password) {
		Optional<User> isUser = userRepo.findById(userObj.getId());
		
		if(!isUser.isPresent()) {
			System.out.println("This user does not exist...");
		}
		else {
			User newUser = new User();
			newUser.setFirstName(userObj.getFirstName());
			newUser.setLastName(userObj.getLastName());
			newUser.setEmail(userObj.getEmail());
			newUser.setPassword(password);
			userRepo.save(newUser);
		}
	}
}
