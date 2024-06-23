package org.choongang.member.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.*;
import org.choongang.member.services.JoinService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final JoinService joinService;

    @GetMapping
    public String index(HttpServletRequest request) {
        String attr = (String) request.getAttribute("commonValue");
       // System.out.println(attr);
        /*
        boolean bool = true;
        if (bool) {
            throw new RuntimeException("테스트1212121212");
        }

         */
        return "member/index";
    }

    @GetMapping("/{mode}/test/{num}")
    public String join(@PathVariable("mode") String mode, @RequestParam("seq") int seq, RequestJoin form,  HttpServletResponse response, @PathVariable("num") int num) {
        System.out.printf("mode=%s, seq=%d, num=%d%n", mode, seq, num);
        System.out.println(form);
        joinService.process();
        return "member/join";
    }

    /*
    @ExceptionHandler({RuntimeException.class})
    public String errorHandler(RuntimeException e1, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(request);
        System.out.println(response);
        System.out.println(session);

        return "errors/error";
    } */
}
