package com.creditsuisse.orderbook.beans;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Bean class used for executing a particular Execution request
 * @author afernandeza
 *
 */
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ExecutionRequest {
	@NotBlank
	private String instrumentId;
	@Range(min = 1, max = 1000000)
	private Long quantity;
	@Range(min = 0, max = 1000000)
	private Double price;
	
    public ExecutionRequest(String instrumentId, long quantity, double price){
    	this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.price = price;
    }	
    
    @Override
    public String toString() {    	
    	return new StringBuilder(200).append("instrumentId:").append(instrumentId).append(", quantity:").append(quantity).append(", price:").append(price).toString();
    }
}
