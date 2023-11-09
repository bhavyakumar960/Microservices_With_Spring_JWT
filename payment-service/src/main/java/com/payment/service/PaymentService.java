package com.payment.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepository;
	
	
	public Payment  doPayment(Payment payment) {
		payment.setTransactionId(UUID.randomUUID().toString());
		payment.setPaymentStatus(paymentProcessingStatus());
		
		if(payment.getPaymentStatus().equals("success")) {
			return paymentRepository.saveAndFlush(payment);
		}
		return payment;
	}
	

	public String paymentProcessingStatus() {
		//here we can use 3rd party payment gateway api(paypal, paytm)
		return new Random().nextBoolean()?"success":"failure";
	}


	public List<Payment> getPaymentByOrderId(Integer orderId) {
		return paymentRepository.findByOrderId(orderId);
	}
	
}
