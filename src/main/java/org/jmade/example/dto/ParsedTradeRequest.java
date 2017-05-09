package org.jmade.example.dto;

import org.jmade.core.message.ACMessage;

public class ParsedTradeRequest {

    private ACMessage ACMessage;
    private TradeRequest tradeRequest;

    public ParsedTradeRequest(ACMessage ACMessage, TradeRequest tradeRequest) {
        this.ACMessage = ACMessage;
        this.tradeRequest = tradeRequest;
    }

    public ACMessage getACMessage() {
        return ACMessage;
    }

    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }
}
