package com.cloud.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@RefreshScope //it is used to dynamically get the refreshed value of microservice.payment-service.endpoints.endpoint.url1 from config server every time.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudConfig {
	
	@Value("${microservice.iam-service.endpoints.endpoint.url1}")
	private String validateTokenURL;

//	@Bean
//	public RouteLocator configureRoute(RouteLocatorBuilder builder) {
//	       return builder.routes()
//	      .route("payment", r->r.path("/api/payment/**").uri("lb://PAYMENT-SERVICE"))
//	      .route("swiggy", r->r.path("/api/swiggy/**").uri("lb://SWIGGY-SERVICE"))
//	      .route("payment", r->r.path("/api/restaurant/**").uri("lb://RESTAURANT-SERVICE"))
//	      .route("iam", r->r.path("/iam/api/**").uri("lb://IDENTITY-SERVICE"))
//	      .route("order", r->r.path("/api/order/**").uri("lb://ORDER-SERVICE")) //dynamic routing -> because we are specifying applicationName
//	      .build();
//	}
	
//	@Bean
//	@LoadBalanced
//    public RestTemplate template(){
//       return new RestTemplate();
//    }
	
	@Bean
	public WebClient.Builder resources(){
		return WebClient.builder();
	}
}
