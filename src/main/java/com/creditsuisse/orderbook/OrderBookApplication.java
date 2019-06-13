package com.creditsuisse.orderbook;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Entry point for the application with Spring Boot
 * @author afernandeza
 *
 */
@EnableConfigurationProperties
@SpringBootApplication
@EnableSwagger2
@Slf4j
public class OrderBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderBookApplication.class, args);
    }

	@Configuration
	@Profile("local")
	@ComponentScan(lazyInit = true)
	static class LocalConfig {
	}

	@PreDestroy
	public void destroy() {
		log.info("Closing order-book-app... wait ...");
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.creditsuisse.orderbook"))
				.paths(PathSelectors.any()).build().apiInfo(metaData());
	}

	// http://localhost:8090/swagger-ui.html
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Order Book Application").description("Application to work with order books")
				.version("0.1.0").build();
	}    
}
