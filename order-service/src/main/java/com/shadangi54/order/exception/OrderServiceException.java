package com.shadangi54.order.exception;

public class OrderServiceException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public OrderServiceException(String message) {
		super(message);
	}

	public OrderServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderServiceException(Throwable cause) {
		super(cause);
	}
}
