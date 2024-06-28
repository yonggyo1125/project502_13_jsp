package org.choongang.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.exceptions.BoardConfigNotFoundException;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PathVariable;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardConfigInfoService configInfoService;
    private final HttpServletRequest request;

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId) {
        commonProcess(bId);

        return "board/list";
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") long seq) {

        return "board/view";
    }

    @GetMapping("/write/{bId}")
    public String write(@PathVariable("bId") String bId) {
        commonProcess(bId);

        return "board/write";
    }

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") long seq) {

        return "board/update";
    }

    /**
     * 모든 요청 처리 메서드에 공통 처리 부분
     *
     * @param bId : 게시판 설정 유지 / 게시판 설정 X -> 게시판이 없음(BoardConfigNotFoundException)
     */
    private void commonProcess(String bId) {
        Board board = configInfoService.get(bId).orElseThrow(BoardConfigNotFoundException::new);

        request.setAttribute("board", board);
    }
}
