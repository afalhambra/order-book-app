package com.creditsuisse.orderbook.exceptions;

/**
 * Exception class to thrown different error scenarios for an Order
 * @author afernandeza
 *
 */
public class OrderException extends ApiException{

	private static final long serialVersionUID = -1509318405295294638L;
	public  static final String ORDER_NOT_FOUND = "ORDER_NOT_FOUND";
	
	private String errorCode;
	
	public OrderException(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
}
