//package com.cloud.gateway.exception.handler;
//
//import java.util.Map;
//
//import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
//import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
//import org.springframework.boot.web.error.ErrorAttributeOptions;
//import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//import reactor.core.publisher.Mono;
//
//@Component
//public class GatewayExceptionHandler extends AbstractErrorWebExceptionHandler {
//
//	public GatewayExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
//			ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
//		super(errorAttributes, resources, applicationContext);
//		this.setMessageReaders(configurer.getReaders());
//		this.setMessageWriters(configurer.getWriters());
//	}
//
//	@Override
//	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//		return RouterFunctions.route(RequestPredicates.all(), this::renderException);
//	}
//	
//	private Mono<ServerResponse> renderException(ServerRequest request){
//		Map<String, Object> error = getErrorAttributes(request, ErrorAttributeOptions.of(Include.MESSAGE));
//		return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(error));
//	}
//	
//	
//
//}
