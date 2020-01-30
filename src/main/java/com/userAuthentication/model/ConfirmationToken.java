package com.userAuthentication.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

@Entity
public class ConfirmationToken {

	@Id
	@GeneratedValue
	@Column(name = "token_id")
	private int tokenId;
	
	@Column(name = "confirmation_token")
	private String confirmationToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	//One to one relationship between token and user
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn( name = "user_id") //join column by using id of user
	private User user;
	
	public ConfirmationToken(User user) {
		this.user = user;
		createdDate = new Date();
		confirmationToken = UUID.randomUUID().toString();
	}
	
	public ConfirmationToken() {
		createdDate = new Date();
	}
	
	
	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getConfirmationToken() {
		return this.confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
	
	
}
