package org.jmade.example.ping;

import org.jmade.core.Agent;
import org.jmade.core.action.Action;
import org.jmade.core.message.ACMessage;

import java.util.Arrays;
import java.util.List;

public class PongAgent extends Agent {
    public PongAgent(String id) {
        super(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        PongAction action = new PongAction(this, Arrays.asList(this.getId()));
        addAction(action);
    }

    private class PongAction extends Action {

        public PongAction(Agent agent, List<String> channelNames) {
            super(agent, channelNames);
        }

        @Override
        public void onMessageReceived(ACMessage message) {
            if(message.getContent().equals("PING")) {
                send("ping", "PONG");
            }
        }
    }
}