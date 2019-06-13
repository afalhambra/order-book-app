package com.creditsuisse.orderbook.repos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.creditsuisse.orderbook.beans.OrderBook;
import com.creditsuisse.orderbook.exceptions.OrderBookException;

/**
 * Repository for an Order Book
 * @author afernandeza
 *
 */
@Repository
public class OrderBookDAO implements IOrderBookDAO {

	private Map<String, OrderBook> orderBookMap = new HashMap<>();

	public OrderBook getOrderBook(String instrumentId) {
		return orderBookMap.get(instrumentId);
	}

	synchronized public void openOrderBook(String instrumentId) throws OrderBookException {
		if (!orderBookMap.containsKey(instrumentId)) {
			OrderBook orderBook = new OrderBook(instrumentId);
			orderBookMap.put(instrumentId, orderBook);
		} else {
			throw new OrderBookException(OrderBookException.ORDER_BOOK_ALREADY_OPENED);
		}
	}
}
