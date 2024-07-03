package org.choongang.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardConfigNotFoundException;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.exceptions.GuestPasswordCheckException;
import org.choongang.board.services.BoardAuthService;
import org.choongang.board.services.BoardDeleteService;
import org.choongang.board.services.BoardInfoService;
import org.choongang.board.services.BoardSaveService;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.global.ListData;
import org.choongang.global.config.annotations.*;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.exceptions.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardConfigInfoService configInfoService;
    private final BoardSaveService saveService;
    private final BoardInfoService infoService;
    private final BoardDeleteService deleteService;
    private final BoardAuthService authService;

    private final HttpServletRequest request;

    private BoardData boardData;
    private Board board;

    @GetMapping("/list/{bId}")
    public String list(@PathVariable("bId") String bId, BoardSearch search) {
        commonProcess(bId, "list");

        ListData<BoardData> data = infoService.getList(bId, search);
        request.setAttribute("items", data.getItems());
        request.setAttribute("pagination", data.getPagination());

        return "board/list";
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") long seq) {
        commonProcess(seq, "view");

        String bId = boardData.getBId();
        ListData<BoardData> data = infoService.getList(bId);
        request.setAttribute("items", data.getItems());
        request.setAttribute("pagination", data.getPagination());

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
        commonProcess(seq, "update");

        RequestBoardData data = infoService.getForm(boardData);
        request.setAttribute("data", data);

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

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") long seq) {
        commonProcess(seq, "delete");

        deleteService.delete(seq);

        return "redirect:/board/list/" + board.getBId();
    }

    @PostMapping("/password")
    public String password(@RequestParam("seq") long seq, @RequestParam("password") String password, HttpSession session) {

        if (password == null || password.isBlank()) {
            throw new AlertException("비밀번호를 입력하세요.", HttpServletResponse.SC_BAD_REQUEST);
        }

        if (!authService.passwordCheck(seq, password)) { // 비회원 비밀번호가 일치하지 않는 경우
            throw new AlertException("비밀번호가 일치하지 않습니다.", HttpServletResponse.SC_BAD_REQUEST);
        }

        // 비밀번호가 일치한다면 인증 처리
        String authKey = "board_" + seq;
        session.setAttribute(authKey, true);

        String script = "parent.location.reload();";
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
        board = configInfoService.get(bId).orElseThrow(BoardConfigNotFoundException::new);
        infoService.setBoard(board);

        // 권한 체크
        long seq = boardData == null ? 0L : boardData.getSeq();
        authService.check(bId, seq, mode);

        // mode가 null이면 write로 기본값 설정
        mode = Objects.requireNonNullElse(mode, "write");

        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        addCss.add("board/style"); // 모든 게시판의 공통 스타일

        if (mode.equals("write") || mode.equals("update")) { // 쓰기, 수정
            addCss.add("board/form");
            addScript.add("ckeditor5/ckeditor");
            addScript.add("fileManager");
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

    /**
     * 게시글 번호가 있는 페이지 URL
     *  - 게시글 보기, 게시글 수정
     *
     * @param seq
     * @param mode
     */
    private void commonProcess(long seq, String mode) {
        boardData = infoService.get(seq).orElseThrow(BoardNotFoundException::new);
        String bId = boardData.getBId();

        commonProcess(bId, mode);

        request.setAttribute("data", boardData);

    }

    @ExceptionHandler(GuestPasswordCheckException.class)
    public String guestPassword() {

        return "board/password";
    }
}
