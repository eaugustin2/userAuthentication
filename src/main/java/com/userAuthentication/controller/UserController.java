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
	public String processLoginForm (@ModelAttribute @Valid User user, Errors errors, Model model) {
		boolean checkUser = userService.isUser(user.getEmail());
		User regUser = userService.getUser(user.getEmail());
		
		if(checkUser == false) {
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
		if(errors.hasErrors()) {
			return "register";
		}
		
		userService.createUser(newUser);
		return "index";
	}
}
