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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Seller extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(Seller.class);

    private static final int ROUNDS_MAX = 64;

    private int current_round = 0;
    private CountDownLatch latch;
    private SortedMap<Double, ACLMessage> buyRequest;
    ObjectMapper objectMapper = new ObjectMapper();

    private Double money = 0.0;

    public Seller(String topic) {
        super(topic);
    }

    @Override
    public void onStart() {
        super.onStart();
        goTradeRounds();
    }

    private void goTradeRounds() {
        try {
            Thread.sleep(2000);
            while (current_round < ROUNDS_MAX) {
                trade();
            }
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setType(TradeRoundConstants.TRADE_FINISHED);
            broadcast(objectMapper.writeValueAsString(tradeRequest));

            System.err.println("********************SELLER***************************");
            System.err.println(getId());
            System.err.println("Money: " + money);
            onStop();

        } catch (InterruptedException | JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    private void trade() throws InterruptedException {
        try {
            sendNotificationRequest();
            buyRequest = new TreeMap<>();
            latch = new CountDownLatch(2);
            int delayTimes = 0;
            while (latch.getCount() > 0 && delayTimes <2) {
                Thread.sleep(500);
                delayTimes++;
            }
            makeDeal();
            current_round++;
            System.err.println("Round finished " + current_round);
        } catch (Exception e) {
            System.err.println("Round not finished");
            e.printStackTrace();
        }
    }

    private void sendNotificationRequest() throws JsonProcessingException {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setType(TradeRoundConstants.ROUND_STARTED);
        tradeRequest.setRound(current_round);
        broadcast(objectMapper.writeValueAsString(tradeRequest));
    }

    private void makeDeal() throws IOException {
        if(buyRequest.size()>0) {
            Double bestPrice = buyRequest.lastKey();
            money += bestPrice;
            ACLMessage aclMessage = buyRequest.get(bestPrice);
            TradeRequest tradeRequest = objectMapper.readValue(aclMessage.getContent(), TradeRequest.class);
            tradeRequest.setType(TradeRoundConstants.SOLD);
            tradeRequest.setPrice(bestPrice);
            reply(aclMessage, objectMapper.writeValueAsString(tradeRequest));
        }
    }

    @Override
    public void onMessageReceived(ACLMessage message) throws IOException {
        TradeRequest tradeRequest = objectMapper.readValue(message.getContent(), TradeRequest.class);
        if (tradeRequest.getType().equals(TradeRoundConstants.BID) && current_round == tradeRequest.getRound()) {
            buyRequest.put(tradeRequest.getPrice(), message);
            latch.countDown();
        }
    }
}
