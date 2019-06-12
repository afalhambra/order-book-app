package com.creditsuisse.orderbook.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.creditsuisse.orderbook.beans.AddOrderRequest;
import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.beans.OrderBook;
import com.creditsuisse.orderbook.beans.OrderDetails;
import com.creditsuisse.orderbook.config.AppConfig;
import com.creditsuisse.orderbook.enums.OrderBookStatus;
import com.creditsuisse.orderbook.enums.OrderType;
import com.creditsuisse.orderbook.exceptions.ApiException;
import com.creditsuisse.orderbook.repos.IOrderDAO;
import com.creditsuisse.orderbook.repos.OrderBookDAO;
import com.creditsuisse.orderbook.service.OrderBookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class LinearQuantityTest {
	@Autowired
    OrderBookService bookService;
	
	@Autowired
	OrderBookDAO orderBookDAO;
	
	@Autowired
	IOrderDAO orderDAO;

	@Test
    public void testQuantityMarketOrder() throws ApiException {
		String instrumentId = "ABC";
		bookService.openOrderBook(instrumentId);
		OrderBook book = bookService.getOrderBook(instrumentId);
		
		assertEquals(book.getInstrumentId(), instrumentId);
		assertEquals(book.getStatus(), OrderBookStatus.OPEN);
		
		bookService.addOrder(new AddOrderRequest(instrumentId, 7L, 34D, OrderType.MARKET));
		bookService.addOrder(new AddOrderRequest(instrumentId, 17L, 34D, OrderType.MARKET)); 
		bookService.addOrder(new AddOrderRequest(instrumentId, 18L, 34D, OrderType.MARKET));
		bookService.closeOrderBook(instrumentId);
		bookService.addExecution(new ExecutionRequest(instrumentId,10,100));
		
		List<OrderDetails> allOrders = orderDAO.getAllOrders(instrumentId);
		
		assertEquals(allOrders.get(0).getExecutionQuantity().longValue(), 2);
		assertEquals(allOrders.get(1).getExecutionQuantity().longValue(), 4);
		assertEquals(allOrders.get(2).getExecutionQuantity().longValue(), 4);	 
		
		bookService.addExecution(new ExecutionRequest(instrumentId,1,100));
		
		assertEquals(allOrders.get(0).getExecutionQuantity().longValue(), 2);
		assertEquals(allOrders.get(1).getExecutionQuantity().longValue(), 4);
		assertEquals(allOrders.get(2).getExecutionQuantity().longValue(), 5);
	}
	
	
	@Test
    public void testQuantityLimitOrder() throws ApiException {
    	String instrumentId = "ABCD";
        bookService.openOrderBook(instrumentId);     
        OrderBook book = bookService.getOrderBook(instrumentId);
        
		assertEquals(book.getInstrumentId(), instrumentId);
		assertEquals(book.getStatus(), OrderBookStatus.OPEN);        
        
   	 	bookService.addOrder(new AddOrderRequest(instrumentId, 1L, 34D, OrderType.LIMIT)); 
   	 	bookService.addOrder(new AddOrderRequest(instrumentId ,1L, 34D,OrderType.LIMIT)); 
		bookService.addOrder(new AddOrderRequest(instrumentId, 1L, 34D, OrderType.LIMIT));
		bookService.addOrder(new AddOrderRequest(instrumentId, 1L, 34D, OrderType.LIMIT));
		bookService.addOrder(new AddOrderRequest(instrumentId, 1L, 34D, OrderType.LIMIT));		
		bookService.closeOrderBook(instrumentId); 
		bookService.addExecution(new ExecutionRequest(instrumentId, 1,100)); 
		 
		List<OrderDetails> allOrders = orderDAO.getAllOrders(instrumentId);
        
        assertEquals(allOrders.get(0).getExecutionQuantity().longValue(), 0);
        assertEquals(allOrders.get(1).getExecutionQuantity().longValue(), 0);
        assertEquals(allOrders.get(2).getExecutionQuantity().longValue(), 0);
        assertEquals(allOrders.get(3).getExecutionQuantity().longValue(), 0);
        assertEquals(allOrders.get(4).getExecutionQuantity().longValue(), 0);
	}
}
