package com.userAuthentication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.userAuthentication.service.EmailService;
import com.userAuthentication.service.UserService;
import com.userAuthentication.model.ConfirmationToken;
import com.userAuthentication.model.ResetPasswordToken;
import com.userAuthentication.model.User;
import com.userAuthentication.repository.ConfirmationTokenRepository;
import com.userAuthentication.repository.ResetPasswordTokenRepository;
import com.userAuthentication.repository.userRepository;

@Controller
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;
	
	@Autowired
	private EmailService emailService;
	
	
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
		
		if(regUser.getIsEnabled() != true) {
			model.addAttribute("loginError", "This account is not validated...");
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
		
		//If passes all tests create user
		//Attach a confirmationToken to user in db
		//save confirmationToken
		userService.createUser(newUser);
		//Search recently created user to save in confirmationToken
		User recentlySavedUser = userService.getUser(newUser.getEmail());
		ConfirmationToken confirmationToken = new ConfirmationToken(recentlySavedUser);
		
		confirmationTokenRepository.save(confirmationToken);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newUser.getEmail());
		mailMessage.setSubject("Verify Email Address");
		mailMessage.setFrom("noreplyuserAuthentication123@gmail.com");
		mailMessage.setText("To confirm email address use link: " + "http:localhost:8080/users/confirm-account?token=" + confirmationToken.getConfirmationToken());
		
		emailService.sendEmail(mailMessage);
		
		
		model.addAttribute("validation","Account Created Successfully, a validation email has been sent to: " + recentlySavedUser.getEmail());
		return "validate";
	}
	
	
	@RequestMapping(value = "confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
	public String confirmAccount(@RequestParam("token") String confirmationToken, Model model) {
		
		System.out.println("test from confirm account...");
		
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		//if this is a valid token given by mail system, enable user
		if(token != null) {
			User user = userService.getUser(token.getUser().getEmail());
			user.setIsEnabled(true);
			userService.updateVerifiedEmailUser(user);
			model.addAttribute("message", "Congratulations " + user.getFirstName() + ", your account has been validated");
			return "confirmation";
		}
		
		model.addAttribute("tokenError", "That is an invalid token...");
		return "confirmation";
		
	}
	
	@GetMapping("forgotPassword")
	public String forgotPassword(Model model) {
		model.addAttribute("title","Reset Password");
		model.addAttribute(new User());
		return "forgotPassword";
	}
	
	//after customer presses link, they should be brought to reset password page to enter a new password, then be brought to index once done
	
	@PostMapping("forgotPassword")
	public String processForgotPasswordForm(@ModelAttribute User user, Model model) {
		//This method will be for processing the data recieved from form in forgot password so the user will get an email then use the link in the email to reset their password
		
		boolean checkUser = userService.isUser(user.getEmail());
		
		if(checkUser == false) {
			model.addAttribute("errorMessage", "This email is not associated with any account");
			return "forgotPassword";
		}
		
		User registeredUser = userService.getUser(user.getEmail());
		ResetPasswordToken resetPasswordToken = new ResetPasswordToken(registeredUser);
		resetPasswordTokenRepository.save(resetPasswordToken);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Reset Password");
		mailMessage.setFrom("noreplyuserAuthentication123@gmail.com");
		mailMessage.setText("To reset your password use link: " + "http:localhost:8080/users/reset-password?resetToken=" + resetPasswordToken.getResetPasswordToken());
		
		emailService.sendEmail(mailMessage);
		model.addAttribute("validation", "A reset password link has been sent to: " + user.getEmail());
		return "validate";
	}
	
	@GetMapping("reset-password")
	public String resetPassword(@RequestParam("resetToken") String resetToken, Model model) {
		//Need to make a resetPassword page
		
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByResetPasswordToken(resetToken);
		
		
		if(resetPasswordToken != null) {
			User user = userService.getUser(resetPasswordToken.getUser().getEmail());
			System.out.println("Name from get mapping: " + user.getFirstName());
			model.addAttribute(user);
			model.addAttribute("message", "Reset Password for " + resetPasswordToken.getUser().getEmail());
			return "resetPassword";
			//need a userService method to update password
		}
		model.addAttribute("validation", "Error, that token is not valid");
		return "validate";
	}
	
	
	
	@PostMapping("reset-password")
	public String processResetPasswordForm(@ModelAttribute User user, Model model) {
		//Call method to reset password and such...
		
		System.out.println("Model Attribute Name from form: " + user.getFirstName());
		
		System.out.println("password from form: " + user.getPassword());
		userService.updatePassword(user);
		model.addAttribute("message", "Password has been successfully changed!");
		return "confirmation";
	}
	
	
	/*
	@RequestMapping(value = "reset-password", method= {RequestMethod.GET, RequestMethod.POST})
	public String resetPassword2(@RequestParam("resetToken") String resetToken, Model model) {
		
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByResetPasswordToken(resetToken);
		
		if(resetPasswordToken != null) {
			User user = userService.getUser(resetPasswordToken.getUser().getEmail());
			userService.updatePassword(user);
			model.addAttribute("message", "Change password for: " + user.getEmail());
			return "resetPassword";
		}
		model.addAttribute("tokenError", "This is an incorrectToken");
		return "confirmation";
	}
	*/
	
}
