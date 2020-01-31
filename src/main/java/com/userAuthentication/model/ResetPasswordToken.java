package com.userAuthentication.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ResetPasswordToken {

	@Id
	@GeneratedValue
	@Column(name = "reset_passwordToken_id")
	private int resetPasswordTokenId;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn( name = "user_id") //join column by using id of user
	private User user;
	
	public ResetPasswordToken() {
		
	}
	
	public ResetPasswordToken(User user) {
		this.user = user;
		resetPasswordToken = UUID.randomUUID().toString();
	}

	public int getResetPasswordTokenId() {
		return resetPasswordTokenId;
	}

	public void setResetPasswordTokenId(int resetPasswordTokenId) {
		this.resetPasswordTokenId = resetPasswordTokenId;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
