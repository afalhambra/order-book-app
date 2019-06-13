package com.creditsuisse.orderbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.creditsuisse.orderbook.beans.ApiResponse;
import com.creditsuisse.orderbook.enums.StatsType;
import com.creditsuisse.orderbook.exceptions.OrderBookException;
import com.creditsuisse.orderbook.service.OrderBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller to work with a particular order book (open, close and collect stats for an order book) 
 * @author afernandeza
 *
 */
@RestController
@RequestMapping("/orderbook")
@Api(value = "Order Book Controller")
@Slf4j
public class OrderBookController {

    @Autowired
    OrderBookService bookService;

    @ApiOperation(value = "openOrderBook", notes = "http://localhost:8081/orderbook/open/{instrumentId}", response = ApiResponse.class)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/open/{instrumentId}")
    public ApiResponse openOrderBook(@PathVariable("instrumentId") String instrumentId){
    	log.info("Open order book for instrument " + instrumentId);
    	ApiResponse resp = new ApiResponse();
        try {
			bookService.openOrderBook(instrumentId);
			resp.success();
		} catch (OrderBookException e) {
			log.error("Error while opening order book for instrument id " + instrumentId + " - " + e.getMessage(), e);
			resp.error(e.getErrorCode());
		}
        return resp;       
    }

    @ApiOperation(value = "closeOrderBook", notes = "http://localhost:8081/orderbook/close/{instrumentId}", response = ApiResponse.class)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/close/{instrumentId}")
    public ApiResponse closeOrderBook(@PathVariable("instrumentId") String instrumentId){
    	log.info("Close order book for instrument " + instrumentId);
    	ApiResponse resp = new ApiResponse();
		try {
			bookService.closeOrderBook(instrumentId);
			resp.success();
		} catch (OrderBookException e) {
			log.error("Error while closing an order book for instrument id " + instrumentId + " - " + e.getMessage(), e);
			resp.error(e.getErrorCode());			
		}			
		return resp;   
    }
    
    @ApiOperation(value = "collectStats", notes = "http://localhost:8081/orderbook/stats/{instrumentId}/{statsType}", response = ApiResponse.class)
    @ResponseStatus(HttpStatus.OK)    
    @GetMapping("stats/{instrumentId}/{statsType}")
    public ApiResponse collectStats(@PathVariable("instrumentId") String instrumentId, @PathVariable("statsType") StatsType statsType) {
    	log.info("Collect " + statsType + " stats for instrument " + instrumentId);
    	ApiResponse resp = new ApiResponse();
    	try {
			resp.setData(bookService.collectStats(instrumentId, statsType));
			resp.success();
		} catch (OrderBookException e) {
			log.error("Error while collecting " + statsType + " stats for instrument id " + instrumentId + " - " + e.getMessage(), e);	
			resp.error(e.getErrorCode());
		}
    	return resp;
    }
}
