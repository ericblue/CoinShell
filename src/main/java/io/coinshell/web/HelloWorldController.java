package io.coinshell.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {


    @GetMapping("/hello")
    public @ResponseBody String sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return "Hello " + name;
    }

}
