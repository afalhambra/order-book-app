package com.creditsuisse.orderbook.beans;

import com.creditsuisse.orderbook.enums.OrderStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Order Details bean class containing details for a particular order
 * @author afernandeza
 *
 */
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderDetails {
	
	private Order order;
	private Long executionQuantity = 0L;
	private boolean isExecutionComplete;
	private OrderStatus status;
	
	public OrderDetails(Order order) {
		this.order = order;
	}
	
	public long getRemainingQuantity() {
		return order.getQuantity() - executionQuantity;
	} 
}
