package com.iam.service.validation;

import org.springframework.stereotype.Component;

import com.iam.dto.UserRequest;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class UserValidation {

	public void checkUserRequestValid(UserRequest request) {
		if(StringUtils.isBlank(request.getEmail())) 
			throw new IllegalArgumentException("Email is required");
		if(StringUtils.isBlank(request.getUsername()))
			throw new IllegalArgumentException("Username is required");
		if(StringUtils.isBlank(request.getRole()))
			throw new IllegalArgumentException("Role is required");
		if(StringUtils.isBlank(request.getPassword()))
			throw new IllegalArgumentException("Password is required");
		if(StringUtils.isBlank(request.getClientId()))
			throw new IllegalArgumentException("ClientId is required");
	}
	
}
