package com.creditsuisse.orderbook.repos;

import com.creditsuisse.orderbook.beans.OrderBook;
import com.creditsuisse.orderbook.exceptions.OrderBookException;

/**
 * Interface defining operations to access a particular Order Book
 * @author afernandeza
 *
 */
public interface IOrderBookDAO {
	public OrderBook getOrderBook(String instrumentId);
	public void openOrderBook(String instrumentId) throws OrderBookException;
}
