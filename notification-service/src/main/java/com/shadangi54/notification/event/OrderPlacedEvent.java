package com.shadangi54.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
	private String customerName;
	private String orderNumber;
}
