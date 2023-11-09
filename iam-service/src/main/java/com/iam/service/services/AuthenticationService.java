package com.iam.service.services;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iam.dto.LoginRequest;
import com.iam.dto.LogoutRequest;
import com.iam.dto.UserRequest;
import com.iam.dto.UserSessionResponse;
import com.iam.dto.ValidateRequest;
import com.iam.mapper.UserMapper;
import com.iam.mapper.UserSessionMapper;
import com.iam.service.entity.User;
import com.iam.service.entity.UserSession;
import com.iam.service.repository.UserRepository;
import com.iam.service.repository.UserSessionRepository;
import com.iam.service.validation.UserSessionValidation;
import com.iam.service.validation.UserValidation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserSessionRepository userSessionRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserSessionValidation sessionValidation;
	@Autowired
	private UserValidation userValidation;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserSessionMapper userSessionMapper;

	final Integer TOKEN_EXPIRATION_TIME = 30;

	@Transactional
	public User saveUser(UserRequest userRequest) {
		userValidation.checkUserRequestValid(userRequest);
		return userRepository.save(userMapper.buildUser(userRequest));
	}

	@Transactional
	public UserSessionResponse generateToken(LoginRequest loginRequest)
			throws NoSuchElementException, AccessDeniedException {
		sessionValidation.validation(loginRequest);

		Optional<User> user = userRepository.fetchUserDetailByUserName(loginRequest.getUsername());
		if (!user.isPresent()) {
			throw new NoSuchElementException("No user exists");
		}
		sessionValidation.checkPasswordValid(loginRequest.getPassword(), user.get().getPassword());

		LocalDateTime time = LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME);
		Optional<UserSession> userSession = userSessionRepository.findActiveSessionById(user.get().getUserId());
		if (userSession.isPresent()) {
			return userSessionMapper.buildLoginResponse(userSession.get());
		}
		String token = jwtService.generateToken(user.get());
		log.info("Token generated is {}", token);
		userSessionRepository.saveAndFlush(userSessionMapper.buildUserSession(user.get(), token, time));
		return userSessionMapper.buildLoginResponse(token, time);
	}

	@Transactional
	public UserSessionResponse validateToken(ValidateRequest validateRequest)
			throws AccessDeniedException, ParseException {
		sessionValidation.validation(validateRequest);
		String sessionToken = validateRequest.getAccess_token();

		Optional<UserSession> session = userSessionRepository.findSessionToken(sessionToken,
				validateRequest.getUser_id());

		if (!session.isPresent()) {
			throw new NoSuchElementException(String.format("No active session for user"));
		}

		if (session.isPresent() && session.get().getTokenExpired()) {
			throw new AccessDeniedException("Token is expired");
		}

		if (!session.get().getToken().equals(validateRequest.getAccess_token())) {
			throw new AccessDeniedException("Token is not valid");
		}

		if (!session.get().getUser_id().equals(validateRequest.getUser_id())) {
			throw new AccessDeniedException("User id is not valid");
		}

		LocalDateTime time = LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_TIME);
		session.get().setTokenExpiry(time);
		userSessionRepository.saveAndFlush(session.get());

		return userSessionMapper.buildLoginResponse(session.get());
	}

	@Transactional
	public String logout(LogoutRequest logoutRequest) throws AccessDeniedException {
		sessionValidation.validation(logoutRequest);
		Optional<UserSession> uOptional = userSessionRepository.findActiveSessionById(logoutRequest.getUserId());
		if (!uOptional.isPresent()) {
			throw new NoSuchElementException("Session not found");
		}
		uOptional.get().setTokenExpired(true);
		userSessionRepository.saveAndFlush(uOptional.get());
		return "User Successfully LoggedOut";
	}

}
