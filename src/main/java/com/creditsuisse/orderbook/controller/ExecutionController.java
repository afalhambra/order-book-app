package com.creditsuisse.orderbook.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.creditsuisse.orderbook.beans.ApiResponse;
import com.creditsuisse.orderbook.beans.ExecutionRequest;
import com.creditsuisse.orderbook.exceptions.ApiException;
import com.creditsuisse.orderbook.service.OrderBookService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller to execute a particular order book
 * @author afernandeza
 *
 */
@RestController
@RequestMapping("/orderbook")
@Api(value = "Execution Controller")
@Slf4j
public class ExecutionController {
	
    @Autowired
    OrderBookService bookService;

    @ApiOperation(value = "execute", notes = "http://localhost:${server.port}/orderbook/execute", response = ApiResponse.class)
    @ApiResponses({ @io.swagger.annotations.ApiResponse(code = 201, message = "success"),
					@io.swagger.annotations.ApiResponse(code = 503, message = "error")})
    @ResponseStatus(HttpStatus.OK)    
    @PostMapping("/execute")
    public ApiResponse execute(@RequestBody @Valid ExecutionRequest executionRequest){
    	log.info("Add execution request [{}]",executionRequest);
    	ApiResponse resp = new ApiResponse();
        try {
			boolean isClosed =  bookService.addExecution(executionRequest);
			resp.success();
			resp.setData(Collections.singletonMap("orderBookedExecuted", isClosed));
		} catch (ApiException e) {
			log.error("Error while executing - " + e.getMessage(), e);
			resp.error(e.getErrorCode());
		}
        return resp;
    }

}
