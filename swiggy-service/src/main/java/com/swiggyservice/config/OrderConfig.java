package com.swiggyservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@RefreshScope //it is used to dynamically get the refreshed value of microservice.payment-service.endpoints.endpoint.url1 from config server every time.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfig {

	@Value("${microservice.restaurant-service.endpoints.endpoint.url1}")
	private String getRestaurantOrder;
	
}
