package org.jmade.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmade.core.Agent;
import org.jmade.core.message.ACLMessage;
import org.jmade.example.dto.TradeRequest;
import org.jmade.example.dto.TradeRoundConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Buyer extends Agent {

    public static final String BROADCAST_TOPIC = "broadcast";
    private static final Logger logger = LoggerFactory.getLogger(Buyer.class);

    private Double money;
    private Integer roundsWon = 0;
    private Double bidIncreasePart;

    private Double currentPrice = 1.0;
    private Boolean isPreviousRoundWon = true;
    private Integer currentRound = -1;

    ObjectMapper objectMapper = new ObjectMapper();

    public Buyer(String topic, Double money, Double currentPrice, Double bidIncreasePart) {
        super(topic);
        this.money = money;
        this.currentPrice = currentPrice;
        this.bidIncreasePart = bidIncreasePart;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscriber.listenToChannel(BROADCAST_TOPIC);
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
        TradeRequest tradeRequest = objectMapper.readValue(message.getContent(), TradeRequest.class);
        if (tradeRequest.getType().equals(TradeRoundConstants.ROUND_STARTED) && tradeRequest.getRound() > currentRound) {
            sendProposal(message, tradeRequest);
            currentRound = tradeRequest.getRound();
        } else if (tradeRequest.getType().equals(TradeRoundConstants.SOLD)) {
            isPreviousRoundWon = true;
            money -= tradeRequest.getPrice();
            roundsWon++;
            tradeRequest.setType(TradeRoundConstants.PAID);
            reply(message, objectMapper.writeValueAsString(tradeRequest));
        } else if (tradeRequest.getType().equals(TradeRoundConstants.TRADE_FINISHED)) {
            System.err.println("****************BUYER*************************");
            System.err.println(getId());
            System.err.println("Money: " + money);
            System.err.println("Rounds won: " + roundsWon);
            onStop();
        }
    }

    private void sendProposal(ACLMessage message, TradeRequest tradeRequest) {
        if (!isPreviousRoundWon) {
            currentPrice = currentPrice + currentPrice * bidIncreasePart;
        }

        isPreviousRoundWon = false;
        if (money >= currentPrice) {
            tradeRequest.setType(TradeRoundConstants.BID);
            tradeRequest.setPrice(currentPrice);
            try {
                reply(message, objectMapper.writeValueAsString(tradeRequest));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
