package org.jmade.platform.topology;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInfo {

    private String hostname;
    private String ip;

    public HostInfo() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
            this.hostname = localHost.getHostName();
            this.ip = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }
}
