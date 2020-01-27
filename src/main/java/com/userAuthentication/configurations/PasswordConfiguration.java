package com.userAuthentication.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfiguration {

	@Bean
	public PasswordEncoder passwordEncrypter() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
		
		//return new BCryptPasswordEncoder();
	}

}
