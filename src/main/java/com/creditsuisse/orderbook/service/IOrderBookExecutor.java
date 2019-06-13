package com.creditsuisse.orderbook.service;

import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.exceptions.OrderBookException;

/**
 * Interface to be implemented by Executor services
 * @author afernandeza
 *
 */
public interface IOrderBookExecutor {

	public boolean execute(ExecutionRequest executionRequest) throws OrderBookException;
	
}
