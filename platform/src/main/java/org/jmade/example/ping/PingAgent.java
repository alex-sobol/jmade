package org.jmade.example.ping;

import org.jmade.core.Agent;
import org.jmade.core.action.Action;
import org.jmade.core.message.ACMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PingAgent extends Agent {
    public PingAgent(String id) {
        super(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        PingAction action = new PingAction(this, Arrays.asList(this.getId()));
        addAction(action);
    }

    private class PingAction extends Action {

        private Date start;
        private int round = 0;

        public PingAction(Agent agent, List<String> channelNames) {
            super(agent, channelNames);
        }

        @Override
        public void onMessageReceived(ACMessage message) {
            if (message.getContent().equals("START")) {
                start = new Date();
                round = 0;
                send("pong", "PING");
            } else if (message.getContent().equals("PONG")) {
                round++;
                if (round > 1000) {
                    send("pong", "Ended:" + (new Date().getTime() - start.getTime()));
                } else {
                    send("pong", "PING");
                }
            }
        }
    }
}
