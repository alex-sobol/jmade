package org.jmade.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agent")
public class AgentController {

    @RequestMapping("/{agentId}")
    public String home(@PathVariable("agentId") String agentId, Model model) {
        model.addAttribute("agentId", agentId);
        return "agent";
    }
}
