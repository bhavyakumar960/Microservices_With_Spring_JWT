package com.swiggyservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiggyservice.client.RestaurantServiceClient;
import com.swiggyservice.dto.BaseResponse;
import com.swiggyservice.dto.OrderResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class SwiggyAppService {

    @Autowired
    private RestaurantServiceClient restaurantServiceClient;

    public String greeting() {
        return "Welcome to Swiggy App Service";
    }

    @CircuitBreaker(name = "getOrderBasedOnOrderId", fallbackMethod = "getErrorMessage")
    public BaseResponse<OrderResponseDTO> checkOrderStatus(String orderId) {
        OrderResponseDTO response = restaurantServiceClient.fetchOrderStatus(orderId);
        
        return BaseResponse.<OrderResponseDTO>builder()
        									   .status(200)
        									   .message("Data Successfully Fetched")
        									   .dataList((response!=null)?Collections.singletonList(response):null)
        									   .build();
    }
    
    
    public BaseResponse<OrderResponseDTO> getErrorMessage(Exception ex){
    	return BaseResponse.<OrderResponseDTO>builder().status(500).message("Sorry Some Error Occured!! "+ex.getMessage()).build();
    }
}