package com.shadangi54.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedEvent {
	private String orderId;
	private String reason;
	private String customerName;
}
