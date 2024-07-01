package org.choongang.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardConfigNotFoundException;
import org.choongang.board.services.BoardSaveService;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.global.config.annotations.*;
import org.choongang.global.exceptions.AlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardConfigInfoService configInfoService;
    private final BoardSaveService saveService;

    private final HttpServletRequest request;

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId) {
        commonProcess(bId, "list");

        return "board/list";
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") long seq) {

        return "board/view";
    }

    @GetMapping("/write/{bId}")
    public String write(@PathVariable("bId") String bId) {
        commonProcess(bId, "write");

        RequestBoardData data = new RequestBoardData();
        data.setBId(bId);

        request.setAttribute("data", data);

        return "board/write";
    }

    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") long seq) {

        return "board/update";
    }

    @PostMapping("/save")
    public String save(RequestBoardData form) {
        String mode = form.getMode();
        String modeStr = mode.equals("update") ? "수정":"등록";
        String message = "게시글 " + modeStr + "에 실패하였습니다";

        BoardData data = saveService.save(form).orElseThrow(() -> new AlertException(message, HttpServletResponse.SC_BAD_REQUEST));

        // 게시글 등록, 수정이 완료 되면 - 게시글 보기로 이동
        String url = request.getContextPath() + "/board/view/" + data.getSeq();
        String script = String.format("parent.location.replace('%s');", url);
        request.setAttribute("script", script);

        return "commons/execute_script";
    }

    /**
     * 모든 요청 처리 메서드에 공통 처리 부분
     *
     * @param bId : 게시판 설정 유지 / 게시판 설정 X -> 게시판이 없음(BoardConfigNotFoundException)
     * @param mode : 처리 모드 - write, update, list, view
     */
    private void commonProcess(String bId, String mode) {
        Board board = configInfoService.get(bId).orElseThrow(BoardConfigNotFoundException::new);

        // mode가 null이면 write로 기본값 설정
        mode = Objects.requireNonNullElse(mode, "write");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        addCss.add("board/style"); // 모든 게시판의 공통 스타일

        if (mode.equals("write") || mode.equals("update")) { // 쓰기, 수정
            addCss.add("board/form");
            addScript.add("ckeditor5/ckeditor");
            addScript.add("board/form");

        } else if (mode.equals("list")) { // 목록
            addCss.add("board/list");

        } else if (mode.equals("view")) { // 글보기
            addCss.add("board/view");

        }

        request.setAttribute("board", board);
        request.setAttribute("addCss", addCss);
        request.setAttribute("addScript", addScript);
    }
}
