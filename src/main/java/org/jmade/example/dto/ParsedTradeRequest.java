package org.jmade.example.dto;

import org.jmade.core.message.ACLMessage;

public class ParsedTradeRequest {

    private ACLMessage aclMessage;
    private TradeRequest tradeRequest;

    public ParsedTradeRequest(ACLMessage aclMessage, TradeRequest tradeRequest) {
        this.aclMessage = aclMessage;
        this.tradeRequest = tradeRequest;
    }

    public ACLMessage getAclMessage() {
        return aclMessage;
    }

    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }
}
