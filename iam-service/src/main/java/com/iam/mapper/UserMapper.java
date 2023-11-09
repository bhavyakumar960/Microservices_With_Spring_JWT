package com.iam.mapper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.iam.dto.UserRequest;
import com.iam.service.entity.User;
import com.iam.utils.DateFormatter;

@Component
public class UserMapper {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public User buildUser(UserRequest request) {
		return User.builder()
				.uuid(UUID.randomUUID().toString())
				.username(request.getUsername())
				.email(request.getEmail())
				.role(request.getRole())
				.password(passwordEncoder.encode(request.getPassword()))
				.isDeleted(false)
				.clientId(request.getClientId())
				.createdDate(DateFormatter.getFormattedDate())
				.createdBy("ADMIN")
				.build();
	}
	
}
