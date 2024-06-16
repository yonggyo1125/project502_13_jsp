package org.choongang.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.member.services.JoinService;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final JoinService joinService;
}
