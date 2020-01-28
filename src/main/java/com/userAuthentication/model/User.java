package com.userAuthentication.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity //specifies that this class mapped to a database
@Table(name = "users") //maps this class to a table named users
public class User {
	
	@Id
	@GeneratedValue //value will automatically be generated for this field
	private int id;
	
	@NotNull
	@Size(min = 2)
	@Column(name = "first_name")
	private String firstName;
	
	@NotNull
	@Size(min = 2)
	@Column(name = "last_name")
	private String lastName;
	
	@NotNull
	@Email
	@Column(name = "email")
	private String email;
	
	@NotNull
	@Size(min = 2)
	@Column(name = "password")
	private String password;
	
	public User() {
		
	}
	
	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setFirstName(String fname) {
		this.firstName = fname;
	}
	
	public void setLastName(String lname) {
		this.lastName = lname;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
