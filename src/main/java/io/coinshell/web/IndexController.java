package io.coinshell.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class IndexController {

    // TODO Resolve issue with view resolver returning 404

    @GetMapping("/")
    public String getIndex() {

        return "index";
    }

}
