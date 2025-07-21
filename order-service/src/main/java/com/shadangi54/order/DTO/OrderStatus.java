package com.shadangi54.order.DTO;

public enum OrderStatus {
	PENDING, COMPLETED, CANCELLED;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
	
	public static OrderStatus fromString(String status) {
		if (status != null) {
			try {
				return OrderStatus.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				// Handle the case where the status is not a valid enum value
			}
		}
		return PENDING; // Default to PENDING if the input is invalid
	}
	
 
}
