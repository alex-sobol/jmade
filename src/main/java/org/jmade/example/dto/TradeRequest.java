package org.jmade.example.dto;

import org.jmade.core.message.ACLMessage;

public class TradeRequest  extends ACLMessage{
    // TODO: Add unique transaction id here or on ACLMessage
    private String type;
    private Double price;
    private Integer round;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
}
