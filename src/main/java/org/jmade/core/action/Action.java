package org.jmade.core.action;

import org.jmade.core.Agent;
import org.jmade.core.message.MessageProcessor;

import java.util.List;

public abstract class Action implements MessageProcessor {

    private Agent agent;
    private List<String> channelNames;

    public Action(Agent agent, List<String> channelNames) {
        this.agent = agent;
        this.channelNames = channelNames;
    }

    public List<String> getChannelNames() {
        return channelNames;
    }
}
