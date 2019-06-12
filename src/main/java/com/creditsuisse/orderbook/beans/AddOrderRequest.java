package com.creditsuisse.orderbook.beans;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import com.creditsuisse.orderbook.enums.OrderType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bean class used for adding orders to a book
 * @author afernandeza
 *
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class AddOrderRequest {
	@NotBlank
	private String instrumentId;
	@Range(min = 1, max = 1000000)
	private Long quantity;
	@Range(min = 0, max = 1000000)
	private Double price;
    OrderType orderType;

    @Override
    public String toString() {    	
    	return new StringBuilder(200).append("instrumentId:").append(instrumentId).append(", quantity:").append(quantity).append(", price:").append(price).append(", ordertype:").append(orderType).toString();
    }
}
