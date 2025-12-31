package com.orderflow.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Result of a trade execution between two orders.
 */
public class TradeResult {
    
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final String symbol;
    private final BigDecimal price;
    private final int quantity;
    private final Instant executedAt;
    
    public TradeResult(String tradeId, String buyOrderId, String sellOrderId,
                       String symbol, BigDecimal price, int quantity, Instant executedAt) {
        // TODO: Implement constructor
        throw new UnsupportedOperationException("Implement constructor");
    }
    
    // TODO: Implement getters
    public String getTradeId() { throw new UnsupportedOperationException(); }
    public String getBuyOrderId() { throw new UnsupportedOperationException(); }
    public String getSellOrderId() { throw new UnsupportedOperationException(); }
    public String getSymbol() { throw new UnsupportedOperationException(); }
    public BigDecimal getPrice() { throw new UnsupportedOperationException(); }
    public int getQuantity() { throw new UnsupportedOperationException(); }
    public Instant getExecutedAt() { throw new UnsupportedOperationException(); }
    
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Implement toString");
    }
}
