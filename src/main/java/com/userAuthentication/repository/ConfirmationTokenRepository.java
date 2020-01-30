package com.userAuthentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.userAuthentication.model.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository <ConfirmationToken, String>{

	ConfirmationToken findByConfirmationToken(String confirmationToken);
	
}
