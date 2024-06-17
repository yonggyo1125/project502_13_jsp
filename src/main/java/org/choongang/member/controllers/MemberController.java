package org.choongang.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.member.services.JoinService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final JoinService joinService;

    @GetMapping("/join")
    public String join() {
        return null;
    }
}
