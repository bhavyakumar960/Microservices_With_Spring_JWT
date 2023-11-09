package com.iam.service.validation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.iam.dto.LoginRequest;
import com.iam.dto.LogoutRequest;
import com.iam.dto.ValidateRequest;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class UserSessionValidation {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
    public void checkPasswordValid(String incommingPassword, String currentPassword) throws AccessDeniedException {
//        if (!currentPassword.equals(incommingPassword)) {
        if (passwordEncoder.matches(incommingPassword, currentPassword)) {
//            throw new AccessDeniedException("Password do not match");
            throw new AccessDeniedException("Username or Password is incorrect");
        }
    }

    public void validation(LoginRequest loginRequest) {
        if (StringUtils.isBlank(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Password is required");
        }

        if (StringUtils.isBlank(loginRequest.getUsername())) {
            throw new IllegalArgumentException("Username is required");
        }
    }


    public void validation(ValidateRequest validateRequest) {
        if (StringUtils.isBlank(validateRequest.getAccess_token())) {
            throw new IllegalArgumentException("Token is required");
        }

        if (validateRequest.getUser_id() == null) {
            throw new IllegalArgumentException("User id is required");
        }
    }
    
    public void validation(LogoutRequest logoutRequest) {
        if (logoutRequest.getUserId() == null) {
            throw new IllegalArgumentException("User id is required");
        }
    }
}
