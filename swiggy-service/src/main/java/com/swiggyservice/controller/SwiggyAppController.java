package com.swiggyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiggyservice.dto.BaseResponse;
import com.swiggyservice.dto.OrderResponseDTO;
import com.swiggyservice.service.SwiggyAppService;

@RestController
@RequestMapping("/api/swiggy")
public class SwiggyAppController {

    @Autowired
    private SwiggyAppService service;

    @GetMapping("/home")
    public String greetingMessage() {
        return service.greeting();
    }

    @GetMapping("/{orderId}")
    public BaseResponse<OrderResponseDTO> checkOrderStatus(@PathVariable String orderId) {
        return service.checkOrderStatus(orderId);
    }
}