package org.jmade.web.controller.page;

import org.jmade.platform.run.AgentRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

@Controller
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    AgentRunner agentRunner;

    @RequestMapping("/{agentId}")
    public String home(@PathVariable("agentId") String agentId, Model model) {
        model.addAttribute("agentId", agentId);
        return "agent";
    }

    @RequestMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadAgent(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        System.out.println(System.getProperty("java.class.path"));
        File root = new File("C:/Temp/");
        File uploadedFile = new File(root, file.getOriginalFilename());
        uploadedFile.getParentFile().mkdirs();
        file.transferTo(uploadedFile);

        /*JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "-classpath ../ -d classes " , uploadedFile.getPath());*/

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{uploadedFile.toURI().toURL()}, this.getClass().getClassLoader());
        Class<?> clazz = classLoader.loadClass("org.jmade.example.ping.PingAgent");
        Class<?> cls = Class.forName("org.jmade.example.ping.PingAgent", true, classLoader);
        Object instance =  cls.newInstance();
        int jj = 0;
        //agentRunner.run(instance);
        return "redirect:/agent/upload";
    }
}
