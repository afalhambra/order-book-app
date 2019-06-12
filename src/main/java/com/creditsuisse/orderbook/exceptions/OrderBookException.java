package com.creditsuisse.orderbook.exceptions;

/**
 * Exception class to thrown different error scenarios for an Order Book
 * @author afernandeza
 *
 */
public class OrderBookException extends ApiException {

	private static final long serialVersionUID = 2629024877442123215L;
	
	public  static final String ORDER_BOOK_NOT_FOUND = "ORDER_BOOK_NOT_FOUND";
	public  static final String ORDER_BOOK_ALREADY_OPENED = "ORDER_BOOK_ALREADY_OPENED";
	public  static final String ORDER_BOOK_ALREADY_CLOSED = "ORDER_BOOK_ALREADY_CLOSED";
	public  static final String ORDER_BOOK_NOT_OPEN = "ORDER_BOOK_NOT_OPEN";
	public  static final String ORDER_BOOK_NOT_CLOSED = "ORDER_BOOK_NOT_CLOSED";
	public  static final String ORDER_BOOK_EXECUTED = "ORDER_BOOK_EXECUTED";
	public  static final String EXECUTION_NOT_APPLIED = "EXECUTION_NOT_APPLIED";
	public  static final String EXECUTION_PRICE_DIFF = "EXECUTION_PRICE_DIFF";
	private String errorCode;
	
	public OrderBookException(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
}
