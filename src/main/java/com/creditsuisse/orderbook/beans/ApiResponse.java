package com.creditsuisse.orderbook.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bean class used for exchanging information between controllers 
 * and clients
 * @author afernandeza
 *
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class ApiResponse {

	private String status;
	
	@JsonInclude(Include.NON_NULL)
	private String errorCode;
	@JsonInclude(Include.NON_NULL)
	private Object data;
	
	public void success() {
		status = "success";
	}
	
	public void error(String errorCode) {
		status = "error";
		this.errorCode = errorCode;
	}
}
