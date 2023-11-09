package com.iam.mapper;


import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.iam.dto.UserSessionResponse;
import com.iam.service.entity.User;
import com.iam.service.entity.UserSession;
import com.iam.utils.DateFormatter;

@Component
public class UserSessionMapper {
	private static final String TOKEN_TYPE = "Bearer";

	public UserSessionResponse buildLoginResponse(String token, LocalDateTime expirationTime) {
		return UserSessionResponse
				.builder()
				.access_token(token)
				.token_expiry(expirationTime.toString())
				.token_type(TOKEN_TYPE)
				.valid(true)
				.build();
	}

	public UserSessionResponse buildLoginResponse(UserSession userSession) {
		return UserSessionResponse
				.builder()
				.access_token(userSession.getToken())
				.token_expiry(userSession.getTokenExpiry().toString())
				.token_type(TOKEN_TYPE)
				.valid(true)
				.build();
	}

	public UserSession buildUserSession(User userDetails, String token, LocalDateTime expirationTime) {
		return UserSession.builder()
				.token(token)
				.tokenType(TOKEN_TYPE)
				.createdDate(DateFormatter.getFormattedDate())
				.createdBy(userDetails.getEmail())
				.tokenExpired(false)
				.tokenExpiry(expirationTime)
				.user_id(userDetails.getUserId())
				.uuid(UUID.randomUUID().toString())
				.build();
	}
}
