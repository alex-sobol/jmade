package org.jmade.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jmade.core.Agent;
import org.jmade.core.action.Action;
import org.jmade.core.message.ACMessage;
import org.jmade.core.message.serialize.JsonConverter;
import org.jmade.core.message.serialize.MessageConverter;
import org.jmade.example.dto.ParsedTradeRequest;
import org.jmade.example.dto.TradeRequest;
import org.jmade.example.dto.TradeRoundConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;

public class Seller extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(Seller.class);

    private static final int ROUNDS_MAX = 64;

    private int current_round = 0;
    private volatile boolean roundFinished = false;
    private List<ParsedTradeRequest> buyRequest;
    MessageConverter<TradeRequest> converter = new JsonConverter<>(TradeRequest.class);

    private Double money = 0.0;

    public Seller(String topic) {
        super(topic);
    }

    @Override
    public void onStart() {
        super.onStart();
        SellAction sellAction = new SellAction(this, Arrays.asList(this.getId()));
        addAction(sellAction);
    }

    private class SellAction extends Action {

        public SellAction(Agent agent, List<String> channelName) {
            super(agent, channelName);
        }

        private void goTradeRounds() {
            Executors.newFixedThreadPool(1).execute(() -> {
                while (current_round < ROUNDS_MAX) {
                    trade();
                }
                TradeRequest tradeRequest = new TradeRequest();
                tradeRequest.setType(TradeRoundConstants.TRADE_FINISHED);
                send(Buyer.BROADCAST_TOPIC, converter.serialize(tradeRequest));

                System.err.println("********************SELLER***************************");
                System.err.println(getId());
                System.err.println("Money: " + money);
                onStop();
            });
        }

        private void trade() {
            try {
                sendNotificationRequest();
                buyRequest = new ArrayList<>();
                int delayTimes = 0;
                while (buyRequest.size() < 2 && delayTimes < 10) {
                    Thread.sleep(100);
                    delayTimes++;
                }
                makeDeal();
                while (!roundFinished) {
                    Thread.sleep(100);
                }
                current_round++;
                System.err.println("Round finished " + current_round);
            } catch (Exception e) {
                System.err.println("Round not finished");
                e.printStackTrace();
            }
        }

        @Override
        public void onMessageReceived(ACMessage message) {
            TradeRequest tradeRequest = converter.deserialize(message.getContent());
            if (tradeRequest.getType().equals(TradeRoundConstants.TRADE_START)) {
                goTradeRounds();
            }
            if (tradeRequest.getType().equals(TradeRoundConstants.BID)) {
                buyRequest.add(new ParsedTradeRequest(message, tradeRequest));
            }
            if (tradeRequest.getType().equals(TradeRoundConstants.PAID)) {
                money += tradeRequest.getPrice();
                roundFinished = true;
            }
        }

        private void sendNotificationRequest() throws JsonProcessingException {
            roundFinished = false;
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setType(TradeRoundConstants.ROUND_STARTED);
            tradeRequest.setRound(current_round);
            send(Buyer.BROADCAST_TOPIC, converter.serialize(tradeRequest));
        }

        private void makeDeal() throws IOException {
            if (buyRequest.size() > 0) {
                SortedMap<Double, ACMessage> sortedRequests = new TreeMap<>();
                buyRequest.forEach(req -> {
                    sortedRequests.put(req.getTradeRequest().getPrice(), req.getACMessage());
                });
                Double bestPrice = sortedRequests.lastKey();
                ACMessage message = sortedRequests.get(bestPrice);
                TradeRequest tradeRequest = converter.deserialize(message.getContent());
                tradeRequest.setType(TradeRoundConstants.SOLD);
                tradeRequest.setPrice(bestPrice);
                send(message.getSenderId(), converter.serialize(tradeRequest));
            } else {
                roundFinished = true;
            }
        }

    }
}
