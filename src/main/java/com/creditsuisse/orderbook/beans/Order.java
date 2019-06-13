package com.creditsuisse.orderbook.beans;

import java.time.LocalDateTime;

import com.creditsuisse.orderbook.enums.OrderType;

import lombok.Getter;

/**
 * Order bean class in the book
 * Can't be modified
 * @author afernandeza
 *
 */
@Getter
public final class Order{
	private String orderId;
	private Long quantity;
	private LocalDateTime entryDate;
	private String instrumentId;
	private Double price;
	private OrderType orderType;    

    public Order(String orderId,String instrumentId, long quantity, double price, OrderType orderType){
    	this.orderId = orderId;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;        
        entryDate = LocalDateTime.now();
    }
}
