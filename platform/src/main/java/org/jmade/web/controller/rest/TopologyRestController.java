package org.jmade.web.controller.rest;

import org.jmade.platform.topology.RegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TopologyRestController {

    @Autowired
    RegistrationUtil registrationUtil;

    @RequestMapping("/topology")
    public Object topology() {
        return registrationUtil.getTopology();
    }
}
