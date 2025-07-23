package com.shadangi54.gateway.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
	@GetMapping
	public ResponseEntity<Map<String, Object>> fallback(){
		return createFallbackResponse("Service is currently unavailable", "unknown-service");
	}
	
	@GetMapping("/product")
    public ResponseEntity<Map<String, Object>> productServiceFallback() {
       return createFallbackResponse("Product Service is currently unavailable", "product-service");
    }

    @GetMapping("/order")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        return createFallbackResponse("Order Service is currently unavailable", "order-service");
    }

    @GetMapping("/inventory")
    public ResponseEntity<Map<String, Object>> inventoryServiceFallback() {
        return createFallbackResponse("Inventory Service is currently unavailable", "inventory-service");
    }
    
    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message, String serviceName) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("service", serviceName);
        response.put("status", "CIRCUIT_BREAKER_OPEN");
        response.put("timestamp", LocalDateTime.now());
        response.put("suggestion", "Please try again later or check service health");
        response.put("errorCode", "SERVICE_UNAVAILABLE");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
