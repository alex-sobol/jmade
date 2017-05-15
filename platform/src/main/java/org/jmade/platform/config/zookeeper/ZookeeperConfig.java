package org.jmade.platform.config.zookeeper;

import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {

    @Autowired
    ZookeeperProps props;

    @Bean
    public ZkClient getZkClient() {
        return new ZkClient(props.getZkServers(), props.getSessionTimeout(), props.getConnectionTimeout(),
                ZKStringSerializer$.MODULE$);
    }

    @Bean
    public RegistrationUtil getRegistrationUtil() {
        return new RegistrationUtil(getZkClient());
    }
}
