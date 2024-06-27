package org.choongang.admin.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.services.config.BoardConfigSaveService;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/board")
public class BoardController {

    private final BoardConfigSaveService saveService;
    private final HttpServletRequest request;

    // 게시판 목록
    @GetMapping
    public String boardList() {

        return "admin/board/index";
    }

    // 게시판 등록
    @GetMapping("/register")
    public String register() {

        request.setAttribute("data", new RequestBoard());

        return "admin/board/register";
    }

    @PostMapping("/save")
    public String save(RequestBoard form) {

        saveService.process(form);

        String url = request.getContextPath() + "/admin/board";
        String script = String.format("parent.location.replace('%s');", url);

        request.setAttribute("script", script);
        return "commons/execute_script";
    }
}
