package com.iam.service.controller;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iam.dto.LoginRequest;
import com.iam.dto.LogoutRequest;
import com.iam.dto.UserRequest;
import com.iam.dto.UserSessionResponse;
import com.iam.dto.ValidateRequest;
import com.iam.response.BaseResponse;
import com.iam.service.entity.User;
import com.iam.service.services.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/iam/api")
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;
	
	
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
		BaseResponse<User> baseResponse = new BaseResponse<User>();
		try {
			User user = authenticationService.saveUser(userRequest);
			return baseResponse.sendResponse(HttpStatus.OK, "Data Successfully Inserted", user, null);
		} catch (DataIntegrityViolationException | IllegalArgumentException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null, null);
		}catch (Exception e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null, null);
		}
	}
	
	@PostMapping(value="/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getToken(@RequestBody LoginRequest loginRequest) {
		BaseResponse<UserSessionResponse> baseResponse = new BaseResponse<>();
		try {
			UserSessionResponse userResponse = authenticationService.generateToken(loginRequest);
			return baseResponse.sendResponse(HttpStatus.OK, "Data Successfully Fetched", userResponse, null);
		}catch (NoSuchElementException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.NOT_FOUND, e.getMessage(), null, null);
		}catch (DataIntegrityViolationException | IllegalArgumentException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null, null);
		}catch (AccessDeniedException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.FORBIDDEN, e.getMessage(), null, null);
		}catch (Exception e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null, null);
		}
	}

	@PostMapping(value = "/validate-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateToken(@RequestBody ValidateRequest validateRequest) {
		log.info("ValidateRequest obj is {}", validateRequest.toString());
		BaseResponse<UserSessionResponse> baseResponse = new BaseResponse<>();
		try {
			UserSessionResponse response = authenticationService.validateToken(validateRequest);
			log.info("UserSessionResponse obj is {}", response.toString());
			return baseResponse.sendResponse(HttpStatus.OK, "Data Successfully Fetched", response, null);
		}catch (NoSuchElementException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.NOT_FOUND, e.getMessage(), null, null);
		}catch (DataIntegrityViolationException | IllegalArgumentException e) {
			log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null, null);
        }catch (AccessDeniedException e) {
        	log.warn(e.toString());
        	return baseResponse.sendResponse(HttpStatus.FORBIDDEN, e.getMessage(), null, null);
        }catch (Exception e) {
        	log.warn(e.toString());
			return baseResponse.sendResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null, null);
		}
	}
	
	@PostMapping(path = "/logout-user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
		BaseResponse<String> baseResponse = new BaseResponse<String>();
        try {
            String message = authenticationService.logout(logoutRequest);
            return baseResponse.sendResponse(HttpStatus.OK, message, null, null);
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
        	log.warn(e.toString());
        	return baseResponse.sendResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null, null);
        } catch (AccessDeniedException e) {
        	log.warn(e.toString());
        	return baseResponse.sendResponse(HttpStatus.FORBIDDEN, e.getMessage(), null, null);
        } catch (NoSuchElementException e) {
        	log.warn(e.toString());
        	return baseResponse.sendResponse(HttpStatus.NOT_FOUND, e.getMessage(), null, null);
        } catch (Exception e) {
        	log.warn(e.toString());
        	return baseResponse.sendResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null, null);
        }
    }
	
}
