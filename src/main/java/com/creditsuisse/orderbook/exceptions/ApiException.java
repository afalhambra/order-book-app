package com.creditsuisse.orderbook.exceptions;

/**
 * Exception class to be raised to the clients consuming the API
 * @author afernandeza
 *
 */
public class ApiException extends Exception{

	private static final long serialVersionUID = 8473459246093896576L;

	public String getErrorCode() {
		return "API_ERROR";
	}
	
}
