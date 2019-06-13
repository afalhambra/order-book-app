package com.creditsuisse.orderbook.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.creditsuisse.orderbook.beans.AddOrderRequest;
import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.beans.OrderBook;
import com.creditsuisse.orderbook.beans.OrderDetails;
import com.creditsuisse.orderbook.beans.OrderbookStatistics;
import com.creditsuisse.orderbook.config.AppConfig;
import com.creditsuisse.orderbook.enums.OrderBookStatus;
import com.creditsuisse.orderbook.enums.OrderType;
import com.creditsuisse.orderbook.enums.StatsType;
import com.creditsuisse.orderbook.exceptions.ApiException;
import com.creditsuisse.orderbook.repos.IOrderDAO;
import com.creditsuisse.orderbook.repos.OrderBookDAO;
import com.creditsuisse.orderbook.service.OrderBookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@TestPropertySource("classpath:application.properties")
public class OrderBookStatsTest {

	@Autowired
    OrderBookService bookService;
	
	@Autowired
	OrderBookDAO orderBookDAO;
	
	@Autowired
	IOrderDAO orderDAO;
		
	@Test
    public void testStats() throws InterruptedException, ApiException {
    	String instrumentId = "ABCD";
        bookService.openOrderBook(instrumentId);     
        OrderBook book = bookService.getOrderBook(instrumentId);
        
		assertEquals(book.getInstrumentId(), instrumentId);
		assertEquals(book.getStatus(), OrderBookStatus.OPEN);
        
   	 	bookService.addOrder(new AddOrderRequest(instrumentId, 10L, 45D, OrderType.LIMIT));   	 	
   	 	bookService.addOrder(new AddOrderRequest(instrumentId ,100L, 23D,OrderType.LIMIT));   	
		bookService.addOrder(new AddOrderRequest(instrumentId, 67L, 54D, OrderType.LIMIT));		
		bookService.addOrder(new AddOrderRequest(instrumentId, 25L, 50D, OrderType.LIMIT));		
		bookService.addOrder(new AddOrderRequest(instrumentId, 25L, 50D, OrderType.LIMIT));		
		bookService.closeOrderBook(instrumentId); 
		bookService.addExecution(new ExecutionRequest(instrumentId, 1,200));
		 
		OrderbookStatistics stats = bookService.collectStats(instrumentId, StatsType.ALL);
		List<OrderDetails> allOrders = orderDAO.getAllOrders(instrumentId);
		
		assertEquals(allOrders.get(1).getOrder().getOrderId(), stats.getMaxOrder().getOrderId());
		assertEquals(allOrders.get(0).getOrder().getOrderId(), stats.getMinOrder().getOrderId());
       
	}
}
