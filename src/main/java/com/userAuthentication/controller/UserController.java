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
	
	//Might put index post for login
	@PostMapping("login")
	public String processLoginForm () {
		
		return ""; //redirect to home page if get user is not null
	}
	
	@GetMapping("register")
	public String registrationPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "Register Page");
		return "register";
	}
	
	@PostMapping("register")
	public String processRegisterForm(@ModelAttribute @Valid User newUser, Errors errors, Model model) {
		
		if(errors.hasErrors()) {
			return "register";
		}
		
		userService.createUser(newUser);
		return "index";
	}
}
