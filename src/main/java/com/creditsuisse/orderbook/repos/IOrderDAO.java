package com.creditsuisse.orderbook.repos;

import java.util.List;
import java.util.function.Consumer;

import com.creditsuisse.orderbook.beans.AddOrderRequest;
import com.creditsuisse.orderbook.beans.Order;
import com.creditsuisse.orderbook.beans.OrderDetails;
import com.creditsuisse.orderbook.exceptions.OrderBookException;
import com.creditsuisse.orderbook.exceptions.OrderException;

/**
 * Interface defining operations to access a particular Order
 * @author afernandeza
 *
 */
public interface IOrderDAO {	
	public Order addOrder(AddOrderRequest order) throws OrderBookException;
    public OrderDetails fetchOrder(String orderId) throws OrderBookException, OrderException;    
    public void updateOrderValidity(String instrumentId, Consumer<OrderDetails> validator) throws OrderBookException;    
    public long getTotalValidUnExecutedOrderQuantity(String instrumentId) throws OrderBookException;   
    public List<OrderDetails> getAllValidOrders(String instrumentId) throws OrderBookException;
    public List<OrderDetails> getAllInValidOrders(String instrumentId) throws OrderBookException;
    public List<OrderDetails> getAllOrders(String instrumentId) throws OrderBookException;
	public void applyExecution(String instrumentId, Consumer<OrderDetails> applier);    
}
