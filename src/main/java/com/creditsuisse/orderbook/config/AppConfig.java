package com.creditsuisse.orderbook.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class for Spring boot
 * @author afernandeza
 *
 */
@Configuration
@ComponentScan(basePackages = {"com.creditsuisse.orderbook"})
public class AppConfig {
}
