package com.swiggyservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.swiggyservice.config.OrderConfig;
import com.swiggyservice.dto.OrderResponseDTO;


@Component
public class RestaurantServiceClient {
    @Autowired
    private RestTemplate template;
    @Autowired
    private OrderConfig config;

    public OrderResponseDTO fetchOrderStatus(String orderId) {
        return template.getForObject(config.getGetRestaurantOrder() + orderId, OrderResponseDTO.class);
    }
}