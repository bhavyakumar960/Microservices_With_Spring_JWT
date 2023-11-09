package com.cloud.gateway.filter;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.cloud.gateway.config.CloudConfig;
import com.cloud.gateway.dto.BaseResponse;
import com.cloud.gateway.dto.ExtractTokenDTO;
import com.cloud.gateway.dto.UserSessionResponse;
import com.cloud.gateway.dto.ValidateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
//@RefreshScope
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;
	@Autowired
	private CloudConfig cloudConfig;
	
	private ExtractTokenDTO tokenDTO;
	
	
	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) { // if reqUrl does not match with byepass url, it will
																	// enter in this if loop
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					return handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Missing Authorization Header");
				}

				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					final String token = authHeader.substring(7);
					
					tokenDTO = extractToken(token);

					try {
						return validateToken(token)
			                    .flatMap(response -> {
			                    	log.info("Validate Token api response is {}", response.toString());
			                        if (response == null || !response.getData().isValid()) {
			                            return handleResponse(exchange.getResponse(), HttpStatus.BAD_REQUEST, "Invalid Token Found");
			                        }
			                        return chain.filter(exchange);
			                    })
			                    .onErrorResume(e -> {
			                        log.info("Error occurred: {}", e);
			                        return handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Invalid Token Found");
			                    });
					}catch (Exception e) {
						log.info("Error occured after validateToken api call is {}", e);
						return handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Invalid Token Found");
					}
				}
			}
			return chain.filter(exchange);
		});

	}

	
	private Mono<BaseResponse<UserSessionResponse>> validateToken(String token) {
		try {
			return WebClient.create(cloudConfig.getValidateTokenURL()).post()
					.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(ValidateRequest.builder().access_token(token).token_type("Bearer").user_id(Long.valueOf(tokenDTO.getSub())).build()), ValidateRequest.class)
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<BaseResponse<UserSessionResponse>>() {});
		}catch (Exception e) {
			log.info("Error occured while calling api is {}", e);
			return Mono.just(null); // Return a Mono containing null on exception
		}
	}

	private ExtractTokenDTO extractToken(String token) {
		ExtractTokenDTO obj = null;
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String[] chunks = token.split("\\.");
		String payload = new String(decoder.decode(chunks[1]));
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			obj = objectMapper.readValue(payload, ExtractTokenDTO.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("BadRequest : " + e);
		}

		return obj;
	}
	
	private Mono<Void> handleResponse(ServerHttpResponse response, HttpStatus status, String errorMessage) {
		log.info("HandleResponse method calling with error message {}", errorMessage);
		// Create a JSON response with status and message
	    String jsonResponse = "{\"status\": \"" + status.value() + "\", \"message\": \"" + errorMessage + "\"}";

	    response.setStatusCode(status);
	    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

	    // Return a JSON response with the error message
	    return response.writeWith(Mono.just(response.bufferFactory().wrap(jsonResponse.getBytes())));
	}

	public static class Config {

	}
}