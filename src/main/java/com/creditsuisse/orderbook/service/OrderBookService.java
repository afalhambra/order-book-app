package com.creditsuisse.orderbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.creditsuisse.orderbook.beans.AddOrderRequest;
import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.beans.Order;
import com.creditsuisse.orderbook.beans.OrderBook;
import com.creditsuisse.orderbook.beans.OrderDetails;
import com.creditsuisse.orderbook.beans.OrderbookStatistics;
import com.creditsuisse.orderbook.enums.OrderBookStatus;
import com.creditsuisse.orderbook.enums.OrderStatus;
import com.creditsuisse.orderbook.enums.OrderType;
import com.creditsuisse.orderbook.enums.StatsType;
import com.creditsuisse.orderbook.exceptions.ApiException;
import com.creditsuisse.orderbook.exceptions.OrderBookException;
import com.creditsuisse.orderbook.exceptions.OrderException;
import com.creditsuisse.orderbook.repos.IOrderBookDAO;
import com.creditsuisse.orderbook.repos.IOrderDAO;

import lombok.extern.slf4j.Slf4j;

/**
 * Service component to handle the operations for an Order Book
 * @author afernandeza
 *
 */
@Service
@Slf4j
public class OrderBookService {
	
	@Autowired
	IOrderBookDAO orderBookDAO;
	
	@Autowired
	@Qualifier("inMemory")
	IOrderDAO orderDAO;
	
	@Autowired
	@Qualifier("linearExecutor")
	IOrderBookExecutor executor;
    
    public OrderBook getOrderBook(String instrumentId) throws OrderBookException {
    	return orderBookDAO.getOrderBook(instrumentId);
    }

    public void openOrderBook(String instrumentId) throws OrderBookException{
    	log.info("opening orderbook for instrumentId:{}", instrumentId);
    	orderBookDAO.openOrderBook(instrumentId);
    }


    public void closeOrderBook(String instrumentId) throws OrderBookException{
    	log.info("closing orderbook for instrumentId:{}", instrumentId);
    	OrderBook obook = orderBookDAO.getOrderBook(instrumentId);
    	if(obook.getStatus() == OrderBookStatus.OPEN) {
    		obook.setStatus(OrderBookStatus.CLOSED);
    	}else {
    		throw new OrderBookException(OrderBookException.ORDER_BOOK_NOT_OPEN);
    	}
    }

    public String addOrder(AddOrderRequest addRequest) throws OrderBookException{
    	log.info("adding order in orderbook for instrumentId:{}", addRequest.getInstrumentId());
        OrderBook orderBook = orderBookDAO.getOrderBook(addRequest.getInstrumentId());
        if(orderBook.getStatus() == OrderBookStatus.OPEN){            
            Order ord = orderDAO.addOrder(addRequest);
            log.info("order:{} added in orderbook for instrumentId:{}", ord.getOrderId(), addRequest.getInstrumentId());
            return ord.getOrderId();
        }else {
        	throw new OrderBookException(OrderBookException.ORDER_BOOK_NOT_OPEN);
        }       
    }
    
    public OrderDetails fetchOrder(String orderId) throws OrderBookException, OrderException{        
        OrderDetails details = orderDAO.fetchOrder(orderId);
        if(details == null) {
        	throw new OrderException(OrderException.ORDER_NOT_FOUND);
        }else {
        	return details;
        }
    }

    synchronized public boolean addExecution(ExecutionRequest executionRequest) throws ApiException{
    	log.info("adding execution in orderbook for instrumentId:{}", executionRequest.getInstrumentId());
        OrderBook orderBook = orderBookDAO.getOrderBook(executionRequest.getInstrumentId());
        if(orderBook.getStatus() == OrderBookStatus.CLOSED){
        	if(!orderBook.isExecutionStarted()) {
    			orderBook.setExecutionStarted(true);
    			orderBook.setExecutionPrice(executionRequest.getPrice());
    			Consumer<OrderDetails> validator = new Consumer<OrderDetails>() {					
					@Override
					public void accept(OrderDetails ordDetails) {
						if(ordDetails.getOrder().getOrderType() == OrderType.LIMIT && ordDetails.getOrder().getPrice() < executionRequest.getPrice()) {
			    			ordDetails.setStatus(OrderStatus.INVALID);
			    			ordDetails.setExecutionComplete(true);			
						}else {
							ordDetails.setStatus(OrderStatus.VALID);
						}
					}
				};
    			orderDAO.updateOrderValidity(executionRequest.getInstrumentId(), validator);
    		}else if(!executionRequest.getPrice().equals(orderBook.getExecutionPrice())){
    			throw new OrderBookException(OrderBookException.EXECUTION_PRICE_DIFF);
    		}
        	boolean isOrderBookExecuted = executor.execute(executionRequest);
            if(isOrderBookExecuted){
            	orderBook.setStatus(OrderBookStatus.EXECUTED);
            	log.info("orderbook instrumentId:{} executed", executionRequest.getInstrumentId());
            }            
            return isOrderBookExecuted;           	
        }else if(orderBook.getStatus() == OrderBookStatus.OPEN){
        	throw new OrderBookException(OrderBookException.ORDER_BOOK_NOT_CLOSED);
        }else if(orderBook.getStatus() == OrderBookStatus.EXECUTED){
        	throw new OrderBookException(OrderBookException.ORDER_BOOK_EXECUTED);
        }else {
        	throw new ApiException();
        }
    }
    
    public OrderbookStatistics collectStats(String instrumentId, StatsType statsType) throws OrderBookException{
    	log.info("collecting stats for instrumentId:{} statsType:{}", instrumentId, statsType);
    	OrderBook book = orderBookDAO.getOrderBook(instrumentId);    	
    	List<OrderDetails> resultList = null;
    	if(statsType == StatsType.VALID) {
    		resultList = orderDAO.getAllValidOrders(instrumentId);
    	}else if(statsType == StatsType.INVALID) {
    		resultList = orderDAO.getAllInValidOrders(instrumentId);
    	}else {
    		resultList = orderDAO.getAllOrders(instrumentId);
    	}
    	OrderbookStatistics stats = new OrderbookStatistics();
    	stats.setInstrumentId(book.getInstrumentId());    	    	
    	long numOrders = resultList.size();
    	Order ord = numOrders>0?resultList.get(0).getOrder():null;
    	Order minOrder = ord;
    	Order maxOrder = ord;
    	Order firstOrder = ord;
    	Order lastOrder = ord;
    	long demand = 0;
    	Map<Double, Long> priceDemand = new HashMap<>();
    	for(OrderDetails order : resultList) {    		
    		if(order.getOrder().getQuantity() < minOrder.getQuantity()) {
    			minOrder = order.getOrder();
    		}
    		if(order.getOrder().getQuantity() > maxOrder.getQuantity()) {
    			maxOrder = order.getOrder();
    		}
    		if(order.getOrder().getEntryDate().isBefore(firstOrder.getEntryDate())) {
    			firstOrder = order.getOrder();
    		}
    		if(order.getOrder().getEntryDate().isAfter(lastOrder.getEntryDate())) {
    			lastOrder = order.getOrder();
    		}
    		demand += order.getOrder().getQuantity();
    		priceDemand.compute(order.getOrder().getPrice(), (k, v)->v == null? order.getOrder().getQuantity() : v + order.getOrder().getQuantity());    		
    	}	
		stats.setNumOrders(numOrders);
		stats.setDemand(demand);
		stats.setMaxOrder(maxOrder);
		stats.setMinOrder(minOrder);
		stats.setFirstOrder(firstOrder);
		stats.setLastOrder(lastOrder);
		for(Entry<Double, Long> entry : priceDemand.entrySet()) {
			stats.addDemandPrice(entry.getKey(), entry.getValue());
		}
				
		return stats;
    }
}
