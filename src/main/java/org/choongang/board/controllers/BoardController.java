package org.choongang.board.controllers;

import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PathVariable;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId) {

        return "board/list";
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") long seq) {

        return "board/view";
    }

    @GetMapping("/write/{bId}")
    public String write(@PathVariable("bId") String bId) {

        return "board/write";
    }

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") long seq) {

        return "board/update";
    }
}
