package com.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	List<Payment> findByOrderId(Integer orderId);

}
