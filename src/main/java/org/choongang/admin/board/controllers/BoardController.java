package org.choongang.admin.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.board.services.config.BoardConfigSaveService;
import org.choongang.global.config.annotations.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/board")
public class BoardController {

    private final BoardConfigSaveService saveService;
    private final BoardConfigInfoService infoService;
    private final HttpServletRequest request;

    // 게시판 목록
    @GetMapping
    public String boardList() {

        List<Board> items = infoService.getList();
        request.setAttribute("items", items);

        return "admin/board/index";
    }

    // 게시판 등록
    @GetMapping("/register")
    public String register() {

        request.setAttribute("data", new RequestBoard());

        return "admin/board/register";
    }

    // 게시판 수정
    @GetMapping("/update/{bId}")
    public String update(@PathVariable("bId") String bId) {

        RequestBoard form = infoService.getForm(bId);
        request.setAttribute("data", form);

        return "admin/board/update";
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
