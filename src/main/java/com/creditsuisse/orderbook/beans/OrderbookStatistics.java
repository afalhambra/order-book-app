package com.creditsuisse.orderbook.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Order Book Statistics bean class to collect stats
 * @author afernandeza
 *
 */
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class OrderbookStatistics {	
	
	@Getter
	private static class DemandPrice{
		Double price;
		Long demand;
		public DemandPrice(double price, long demand) {
			super();
			this.price = price;
			this.demand = demand;
		}
	}
	
	private String instrumentId;
	private long numOrders;
	private long demand;
	private Order maxOrder;
	private Order minOrder;
	private Order firstOrder;
	private Order lastOrder;
	private List<DemandPrice> limitBreakDown;
	
	public OrderbookStatistics() {
		limitBreakDown = new ArrayList<>();
	}
	
	public void addDemandPrice(double price, long demand) {
		limitBreakDown.add(new DemandPrice(price, demand));
	}
}
