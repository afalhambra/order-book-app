package com.creditsuisse.orderbook.service;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.beans.OrderDetails;
import com.creditsuisse.orderbook.exceptions.OrderBookException;
import com.creditsuisse.orderbook.repos.IOrderDAO;

import lombok.extern.slf4j.Slf4j;

/**
 * Service component to execute a particular order book
 * @author afernandeza
 *
 */
@Component("linearExecutor")
@Slf4j
public class LinearOrderBookExecutor implements IOrderBookExecutor {
	
	@Autowired
	IOrderDAO orderDAO;
	
	@Override
	public boolean execute(ExecutionRequest executionRequest) throws OrderBookException {       
        boolean[] executeOrderBook = {true};
        long totalValidQuantityOut = orderDAO.getTotalValidUnExecutedOrderQuantity(executionRequest.getInstrumentId());
        log.info("total valid unexecuted order quantity:{}",totalValidQuantityOut);
        Consumer<OrderDetails> quantityApplier = new Consumer<OrderDetails>() {
			
        	long totalValidQuantity = totalValidQuantityOut;
        	long executionQuantity = executionRequest.getQuantity();  
        	
			@Override
			public void accept(OrderDetails details) {
				if(executionQuantity > 0 && !details.isExecutionComplete()){
	                float percentage = details.getRemainingQuantity() / (float)totalValidQuantity;
	                long quantity = Math.round(percentage * executionQuantity);
	                totalValidQuantity = totalValidQuantity - details.getRemainingQuantity();
	                executionQuantity = executionQuantity - quantity + updateOrderExecutionQuantity(details, quantity);
	                executeOrderBook[0] &= details.isExecutionComplete();
	            }	            		
			}
		};
		
		orderDAO.applyExecution(executionRequest.getInstrumentId(), quantityApplier);		
        return executeOrderBook[0] && totalValidQuantityOut>0;
	}
	
	/**
	 * 
	 * @param details
	 * @param paramQuantity
	 * @return
	 */
	private long updateOrderExecutionQuantity(OrderDetails details, long paramQuantity) {
		log.info("update order execution quantity:{} - quantity:" + paramQuantity,details);
		long diff = 0;
		long temp = details.getExecutionQuantity() + paramQuantity;		
		if(temp > details.getOrder().getQuantity()){
			diff = temp - details.getOrder().getQuantity();
			details.setExecutionQuantity(details.getOrder().getQuantity());
		}else{
			details.setExecutionQuantity(temp);
		}
		details.setExecutionComplete(details.getExecutionQuantity().equals(details.getOrder().getQuantity()));
		return diff;
	}
}
