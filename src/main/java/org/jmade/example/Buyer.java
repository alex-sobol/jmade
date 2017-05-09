package org.jmade.example;

import org.jmade.core.Agent;
import org.jmade.core.action.Action;
import org.jmade.core.message.ACMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.example.dto.TradeRequest;
import org.jmade.example.dto.TradeRoundConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Buyer extends Agent {

    public static final String BROADCAST_TOPIC = "broadcast";
    private static final Logger logger = LoggerFactory.getLogger(Buyer.class);

    private Double money;
    private Integer roundsWon = 0;
    private Double bidIncreasePart;

    private Double currentPrice = 1.0;
    private Boolean isPreviousRoundWon = true;
    private Integer currentRound = -1;

    MessageConverter<TradeRequest> converter = new JsonConverter<>(TradeRequest.class);

    public Buyer(String topic, Double money, Double currentPrice, Double bidIncreasePart) {
        super(topic);
        this.money = money;
        this.currentPrice = currentPrice;
        this.bidIncreasePart = bidIncreasePart;
    }

    @Override
    public void onStart() {
        super.onStart();
        BuyAction action = new BuyAction(this, Arrays.asList(this.getId(), BROADCAST_TOPIC));
        addAction(action);
    }



    private class BuyAction extends Action{

        public BuyAction(Agent agent, List<String> channelName) {
            super(agent, channelName);
        }

        @Override
        public void onMessageReceived(ACMessage message) {
            TradeRequest tradeRequest = converter.deserialize(message.getContent());
            if (tradeRequest.getType().equals(TradeRoundConstants.ROUND_STARTED) && tradeRequest.getRound() > currentRound) {
                sendProposal(message, tradeRequest);
                currentRound = tradeRequest.getRound();
            } else if (tradeRequest.getType().equals(TradeRoundConstants.SOLD)) {
                isPreviousRoundWon = true;
                money -= tradeRequest.getPrice();
                roundsWon++;
                tradeRequest.setType(TradeRoundConstants.PAID);
                send(message.getSenderId(), converter.serialize(tradeRequest));
            } else if (tradeRequest.getType().equals(TradeRoundConstants.TRADE_FINISHED)) {
                System.err.println("****************BUYER*************************");
                System.err.println(getId());
                System.err.println("Money: " + money);
                System.err.println("Rounds won: " + roundsWon);
                onStop();
            }
        }

        private void sendProposal(ACMessage message, TradeRequest tradeRequest) {
            if (!isPreviousRoundWon) {
                currentPrice = currentPrice + currentPrice * bidIncreasePart;
            }

            isPreviousRoundWon = false;
            if (money >= currentPrice) {
                tradeRequest.setType(TradeRoundConstants.BID);
                tradeRequest.setPrice(currentPrice);
                send(message.getSenderId(), converter.serialize(tradeRequest));
            }
        }
    }
}
