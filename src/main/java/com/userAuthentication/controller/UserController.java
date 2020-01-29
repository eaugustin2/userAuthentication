package com.userAuthentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.userAuthentication.service.UserService;
import com.userAuthentication.model.User;

@Controller
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("login")
	public String indexPage(Model model) {
		model.addAttribute("title", "Main Page");
		model.addAttribute(new User()); //adding user attribute to view to better handle form
		return "index";
	}
	
	/*
	 * This controller needs to verify that email and password given match an email and its corresponding password
	 */
	@PostMapping("login")
	public String processLoginForm (@ModelAttribute @Valid User user, Errors errors, Model model) {
		boolean checkUser = userService.isUser(user.getEmail());
		User regUser = userService.getUser(user.getEmail());
		
		//Checks if they are a user
		if(checkUser == false) {
			model.addAttribute("loginError", "There is no account created with this email");
			return "index";
		}
		
		//need to check if password matches, will also have to check if verified once completed
		if(userService.verifyUser(user) == false) {
			model.addAttribute("loginError", "Password is incorrect");
			return "index";
		}
		
		model.addAttribute("firstName",regUser.getFirstName());
		return "welcome"; //redirect to home page if get user is not null
	}
	
	@GetMapping("register")
	public String registrationPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "Register Page");
		return "register";
	}
	
	@PostMapping("register")
	public String processRegisterForm(@ModelAttribute @Valid User newUser, Errors errors, Model model) {
		System.out.println("made it to the post mapping of register");
		boolean emailInUse = userService.isUser(newUser.getEmail());
		
		if(errors.hasErrors()) {
			return "register";
		}
		
		if(emailInUse == true) {
			model.addAttribute("emailRegistered","This email already has an account");
			return "register";
		}
		
		userService.createUser(newUser);
		model.addAttribute("loginError","Account Created Successfully!");
		return "index";
	}
}
