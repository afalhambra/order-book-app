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
import com.creditsuisse.orderbook.enums.OrderStatus;
import com.creditsuisse.orderbook.enums.OrderType;
import com.creditsuisse.orderbook.exceptions.ApiException;
import com.creditsuisse.orderbook.repos.IOrderDAO;
import com.creditsuisse.orderbook.repos.OrderBookDAO;
import com.creditsuisse.orderbook.service.OrderBookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class OrderBookServiceTest {

    @Autowired
    OrderBookService bookService;
    
    @Autowired
	OrderBookDAO orderBookDAO;
	
	@Autowired
	IOrderDAO orderDAO;

    @Test
    public void testBook() throws ApiException{
    	String instrumentId = "ABC";
        bookService.openOrderBook(instrumentId);
        OrderBook book = bookService.getOrderBook(instrumentId);
        bookService.addOrder(new AddOrderRequest(instrumentId, 90L, 34D, OrderType.LIMIT));
        bookService.addOrder(new AddOrderRequest(instrumentId, 10L, 135D, OrderType.LIMIT));
        bookService.closeOrderBook(instrumentId);
        bookService.addExecution(new ExecutionRequest(instrumentId, 90,100));       
        
        List<OrderDetails> allOrders = orderDAO.getAllOrders(instrumentId);
        assertEquals(allOrders.get(0).getStatus(), OrderStatus.INVALID);
        assertEquals(allOrders.get(1).getStatus(), OrderStatus.VALID);
        
        assertEquals(book.getStatus(), OrderBookStatus.EXECUTED);

    }  
   
}
