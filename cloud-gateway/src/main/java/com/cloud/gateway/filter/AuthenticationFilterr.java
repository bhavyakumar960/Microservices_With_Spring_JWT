//package com.cloud.gateway.filter;
//
//import java.time.LocalDateTime;
//import java.util.Base64;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import com.cloud.gateway.config.CloudConfig;
//import com.cloud.gateway.dto.BaseResponse;
//import com.cloud.gateway.dto.ExtractTokenDTO;
//import com.cloud.gateway.dto.TokenDTO;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//@RefreshScope
//public class AuthenticationFilterr extends AbstractGatewayFilterFactory<AuthenticationFilterr.Config> {
//
//	@Autowired
//	private RouteValidator validator;
//	@Autowired
//	private CloudConfig cloudConfig;
//
//	@Autowired
//	private WebClient.Builder webClientBuilder;
//
//	public AuthenticationFilterr() {
//		super(Config.class);
//	}
//
//	@Override
//	public GatewayFilter apply(Config config) {
//		return ((exchange, chain) -> {
//			if (validator.isSecured.test(exchange.getRequest())) { // if reqUrl does not match with byepass url, it will
//																	// enter in this if loop
//				// header contains token or not
//				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
////					throw new RuntimeException("Missing Authorization Header");
//					return handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Missing Authorization Header");
//				}
//
//				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//				if (authHeader != null && authHeader.startsWith("Bearer ")) {
//					final String token = authHeader.substring(7);
//
//					try {
//						ExtractTokenDTO dto = extractToken(token);
//						// REST call to AUTH service to call the validateToken method
//						validateToken(dto, token)
//					    .subscribe(result -> {
//					        if (!result) {
//					            handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Invalid Token Found");
//					        }
//					    }, throwable -> {
//					        handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Error validating token");
//					    });
//
//					} catch (Exception e) {
//						log.info("Error occured is {}", e);
////						Mono.error(new RuntimeException("Invalid Token"));
//						handleResponse(exchange.getResponse(), HttpStatus.UNAUTHORIZED, "Invalid Token Found");
//					}
//				}
//			}
//			try {
//				Thread.sleep(1000000);
//			} catch (InterruptedException e) {
//				log.info("Thread error {}", e);
//			}
//			System.out.println(LocalDateTime.now());
//			return chain.filter(exchange);
//		});
//
//	}
//
////	public boolean validateToken(ExtractTokenDTO dto, String token) throws Exception {
////		
////		HttpHeaders headers = new HttpHeaders();
////		headers.setContentType(MediaType.APPLICATION_JSON);
////		
////		HttpEntity<TokenDTO> entity = new HttpEntity<TokenDTO>(TokenDTO.builder().token(token).userName(dto.getUsername()).build(),
////				headers);
////		try {
////			BaseResponse<String> response = restTemplate
////					.exchange(cloudConfig.getValidateTokenURL(), HttpMethod.POST, entity, new ParameterizedTypeReference<BaseResponse<String>>() {})
////					.getBody();
////
////			return response != null && response.getMessage().equalsIgnoreCase("Token is Valid") ? true : false;
////		} catch (Exception e) {
////			log.error("Exceptions = {}", e);
////			throw new RuntimeException("Exception occured while calling token-validate api");
////		}
////	}
//
//	public Mono<Boolean> validateToken(ExtractTokenDTO dto, String token) {
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
//
//	    TokenDTO tokenDto = TokenDTO.builder().token(token).userName(dto.getUsername()).build();
//
//	    return webClientBuilder.build()
//	            .post()
//	            .uri(cloudConfig.getValidateTokenURL())
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .bodyValue(tokenDto)
//	            .retrieve()
//	            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
//	                return clientResponse.bodyToMono(String.class)
//	                        .flatMap(responseBody -> {
//	                            log.error("Token validation failed with response: {}", responseBody);
//	                            return Mono.error(new RuntimeException("Token validation failed"));
//	                        });
//	            })
//	            .bodyToMono(new ParameterizedTypeReference<BaseResponse<String>>() {})
//	            .flatMap(response -> {
//	                if (response != null && response.getMessage().equalsIgnoreCase("Token is Valid")) {
//	                    return Mono.just(true);
//	                } else {
//	                    return Mono.just(false);
//	                }
//	            })
//	            .onErrorResume(throwable -> {
//	                log.error("Exception occurred while calling token-validate API", throwable);
//	                return Mono.just(false);
//	            });
//	}
//
//
//
//	private ExtractTokenDTO extractToken(String token) {
//		ExtractTokenDTO obj = null;
//		Base64.Decoder decoder = Base64.getUrlDecoder();
//		String[] chunks = token.split("\\.");
//		String payload = new String(decoder.decode(chunks[1]));
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		try {
//			obj = objectMapper.readValue(payload, ExtractTokenDTO.class);
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException("BadRequest : " + e);
//		}
//
//		return obj;
//	}
//	
//	
//	private Mono<Void> handleResponse(ServerHttpResponse response, HttpStatus status, String errorMessage) {
//		log.info("HandleResponse method calling with error message {}", errorMessage);
//	    response.setStatusCode(status);
//	    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//	    // Return a JSON response with the error message
//	    return response.writeWith(Mono.just(response.bufferFactory().wrap(errorMessage.getBytes())));
//	}
//
//	public static class Config {
//
//	}
//}