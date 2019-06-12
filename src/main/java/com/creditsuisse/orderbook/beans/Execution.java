package com.creditsuisse.orderbook.beans;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Bean Execution class
 * @author afernandeza
 *
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Execution {

    long quantity;
    double price;

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
