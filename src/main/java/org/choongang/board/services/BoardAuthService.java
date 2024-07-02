package org.choongang.board.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.choongang.board.constants.Authority;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardConfigNotFoundException;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.exceptions.AlertException;
import org.choongang.member.MemberUtil;

@Service
@Setter
@RequiredArgsConstructor
public class BoardAuthService {
    private final BoardConfigInfoService configInfoService; // 게시판 설정 조회
    private final BoardInfoService infoService; // 게시글 조회
    private final MemberUtil memberUtil;

    private Board board; // 게시판 설정
    private BoardData boardData; // 게시글

    /**
     * 게시판 권한 체크
     *
     * @param bId : 게시판 ID
     * @param seq : 게시글 번호
     * @param mode : view, list, write, update, delete
     */
    public void check(String bId, long seq, String mode) {
        if (board == null) { // 게시판 설정이 없는 경우 조회
            board = configInfoService.get(bId).orElseThrow(BoardConfigNotFoundException::new);
        }

        if (boardData == null) { // 게시글이 없는 경우 조회
            boardData = infoService.get(seq).orElseThrow(BoardNotFoundException::new);
        }

        // 게시판 설정 - 사용 여부 체크, 관리자는 접근 가능
        if (board.getActive() == 0 && !memberUtil.isAdmin()) {
            throw new AlertException("접근이 불가한 게시판입니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }

        // 게시판 설정 - 글쓰기 권한 체크
        Authority authority = board.getAuthority();
        if (mode.equals("write") && !memberUtil.isLogin() && authority == Authority.USER) { // 회원 전용 게시판

        }
    }
}
