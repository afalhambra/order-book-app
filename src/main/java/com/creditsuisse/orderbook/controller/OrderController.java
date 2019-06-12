package com.creditsuisse.orderbook.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.creditsuisse.orderbook.beans.AddOrderRequest;
import com.creditsuisse.orderbook.beans.ApiResponse;
import com.creditsuisse.orderbook.exceptions.OrderBookException;
import com.creditsuisse.orderbook.exceptions.OrderException;
import com.creditsuisse.orderbook.service.OrderBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller to work with a particular order (add and get order) 
 * @author afernandeza
 *
 */
@RestController
@RequestMapping("/orderbook/order")
@Api(value = "Order Controller")
@Slf4j
public class OrderController {

    @Autowired
    OrderBookService bookService;

    @ApiOperation(value = "addOrder", notes = "http://localhost:${server.port}/orderbook/order/add", response = ApiResponse.class)
    @ApiResponses({ @io.swagger.annotations.ApiResponse(code = 201, message = "success"),
					@io.swagger.annotations.ApiResponse(code = 503, message = "error")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add")
    public ApiResponse addOrder(@RequestBody @Valid AddOrderRequest request){
    	log.info("Add order request [{}]",request);
    	ApiResponse resp = new ApiResponse();
    	try {
			String orderId = bookService.addOrder(request);
			resp.success();
			resp.setData(Collections.singletonMap("orderId", orderId));
		} catch (OrderBookException e) {
			log.error("Error while adding order - " + e.getMessage(), e);
			resp.error(e.getErrorCode());
		}
    	return resp;
    }
    
    @ApiOperation(value = "getOrder", notes = "http://localhost:${server.port}/orderbook/order/get/{orderId}", response = ApiResponse.class)
    @ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "success"),
    				@io.swagger.annotations.ApiResponse(code = 404, message = "error")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/{orderId}")
    public ApiResponse getOrder(@PathVariable("orderId") String orderId){
    	log.info("Get orderId " + orderId);
    	ApiResponse resp = new ApiResponse();
    	try {
    		resp.setData(bookService.fetchOrder(orderId));
			resp.success();
		} catch (OrderBookException | OrderException e) {
			log.error("Error while getting order id " + orderId + " - " + e.getMessage(), e);
			resp.error(e.getErrorCode());
		}
    	return resp;
    }
}
