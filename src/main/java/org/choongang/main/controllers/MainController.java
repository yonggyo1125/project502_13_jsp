package org.choongang.main.controllers;

import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PathVariable;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "main/index";
    }

    @GetMapping("/board/{seq}")
    public String board(@PathVariable("seq") int seq) {
        System.out.println(seq);
        return "main/index";
    }
}
