package com.userAuthentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.userAuthentication.model.ResetPasswordToken;

@Repository
public interface ResetPasswordTokenRepository extends CrudRepository <ResetPasswordToken, String>{
	
	ResetPasswordToken findByResetPasswordToken(String resetPasswordToken);
}
