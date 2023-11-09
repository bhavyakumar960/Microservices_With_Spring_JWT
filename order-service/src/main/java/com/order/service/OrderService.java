package com.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.order.config.OrderConfig;
import com.order.entity.Order;
import com.order.model.PaymentDTO;
import com.order.model.TransactionRequest;
import com.order.model.TransactionResponse;
import com.order.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
//	public static final String URL = "http://PAYMENT-SERVICE/api/payment/doPayment";
	
	@Autowired
	private OrderConfig config;  
//jaise hi order-service load hogi, config server se contact kregi aur config server mein jo application.yml hai
//usse lekr aajayegi aur order-service ki yml mein daal degi to url ab hard-coded nhi rha, jaise hi koi url change krega dev side,
//to same url git pr change krega to yaha bina kisi error url change hojayega automatically
	
	@CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
	public TransactionResponse saveOrder(TransactionRequest request) {
		
		String message = "";
		
		Order order = orderRepository.saveAndFlush(request.getOrder());
		
		//setting payment object
		PaymentDTO paymentDTO = request.getPayment();
		paymentDTO.setOrderId(order.getId());
		paymentDTO.setAmount(order.getPrice());
		
		//do a rest call to set payment and pass orderId
		PaymentDTO paymentResp = restTemplate.postForObject(config.getDoPaymentURL(), request.getPayment(), PaymentDTO.class);
		
		message = paymentResp.getPaymentStatus().equals("success")?"payment successfully done":"some error in payment api, order is set to cart";
		
		return TransactionResponse.builder()
				.order(order)
				.amount(paymentResp.getAmount())
				.transactionId(paymentResp.getTransactionId())
				.message(message)
				.build();
	}
	
	//paymentFallback() method must have same return type of its caller method i.e, saveOrder
	public TransactionResponse paymentFallback(Exception e) {
		return TransactionResponse.builder().message("Payment Service is unable to reach at the moment").build();
	}
}
