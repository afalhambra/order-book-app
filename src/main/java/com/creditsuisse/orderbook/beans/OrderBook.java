package com.creditsuisse.orderbook.beans;

import com.creditsuisse.orderbook.enums.OrderBookStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Order Book bean class for a particular instrument
 * @author afernandeza
 *
 */
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderBook{	

	private String instrumentId;
	private OrderBookStatus status;
	private boolean executionStarted;
	private Double executionPrice;
    
    public OrderBook(String instrumentId){
        this.instrumentId = instrumentId;       
        status = OrderBookStatus.OPEN;       
    }
}
