package com.creditsuisse.orderbook.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller to custom shutdown the application in a gracefull way.
 * This is also available through the default spring actuator components on http://localhost:port/actuator/shutdown
 * @author afernandeza
 *
 */
@RestController
@RequestMapping("/orderbook")
@Api(value = "Shutdown Controller")
@Slf4j
public class ShutdownController implements ApplicationContextAware {
     
    private ApplicationContext context;
     
    @ApiOperation(value = "shutdownContext", notes = "http://localhost:8081/orderbook/shutdown")
    @PostMapping("/shutdown")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void shutdownContext() {
    	log.info("Shutting down the application...");
        ((ConfigurableApplicationContext) context).close();
    }
 
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }
}