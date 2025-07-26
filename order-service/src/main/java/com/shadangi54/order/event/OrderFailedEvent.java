package com.shadangi54.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedEvent {
	private String customerName;
	private String orderId;
	private String reason;
}
