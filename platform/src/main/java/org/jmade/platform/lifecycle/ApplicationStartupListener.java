package org.jmade.platform.lifecycle;

import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${jmade.debug.mode}")
    Boolean isDebugMode;

    @Autowired
    RegistrationUtil registrationUtil;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (isDebugMode) {
            registrationUtil.clean();
        }
        registrationUtil.registerNode();
    }

}