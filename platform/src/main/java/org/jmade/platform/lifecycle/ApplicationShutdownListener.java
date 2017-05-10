package org.jmade.platform.lifecycle;

import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    RegistrationUtil registrationUtil;

    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {
        registrationUtil.deleteNode();
        try {
            registrationUtil.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
