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
	private PasswordEncoder encoder; //this interface is used after it was declared in bean configuration to hash passwords and it also uses a random salt
	
	@Autowired
	userRepository userRepo;
	
	
	/*
	 * Method to check if this user has made an account already with this email address
	 */
	public boolean isUser(String email) {
		User isUser = userRepo.findByEmail(email);
		
		
		if(isUser == null) {
			return false;
		}
		return true;
	}
	
	
	public boolean verifyUser(User u) {
		User real = userRepo.findByEmail(u.getEmail()); //user from repository
		
		if(encoder.matches(u.getPassword(), real.getPassword())) {
			System.out.println("The password matches...");
			return true;
		}
		System.out.println("The password doesn't match...");
		return false;
	}
	
	
	
	/*
	 * Method to create a new user 
	 */
	public void createUser (User userObj) {
		
		//Check if user has been created 
		
		boolean checkUser = isUser(userObj.getEmail());
		
		if(checkUser == false) {
			User newUser = new User();
			newUser.setFirstName(userObj.getFirstName());
			newUser.setLastName(userObj.getLastName());
			newUser.setEmail(userObj.getEmail());
			String hashedPassword = encoder.encode(userObj.getPassword());
			newUser.setPassword(hashedPassword);
			System.out.println("hashed password: " + hashedPassword);
			userRepo.save(newUser);
			System.out.println("New User has been created successfully...");
		}
		else {
			System.out.println("This email address is already in use...");
		}
	}
	
	/*
	 * Method to get user by email
	 */
	public User getUser(String email) {
		User currUser = userRepo.findByEmail(email);
		return currUser;
	}
	
	public void updateVerifiedEmailUser(User user) {
		User savedUser = userRepo.findByEmail(user.getEmail());
		savedUser.setIsEnabled(user.getIsEnabled());
		userRepo.save(savedUser);
	}
	
	/*
	 * Method to update a users password
	 */
	public boolean updatePassword(User userObj) {
		User regUser = userRepo.findByEmail(userObj.getEmail());
		System.out.println("This user is: " + regUser.getFirstName());
		System.out.println("value of password in service: " + userObj.getPassword());
		
		if(encoder.matches(userObj.getPassword(), regUser.getPassword()) == true) {
			return false;
		}
		String hashedPassword = encoder.encode(userObj.getPassword());
		regUser.setPassword(hashedPassword);
		userRepo.save(regUser);
		return true;
	}
}
