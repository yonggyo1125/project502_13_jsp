package org.choongang.board.validators;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.MemberUtil;

@Component
@RequiredArgsConstructor
public class BoardSaveValidator implements Validator<RequestBoardData>, RequiredValidator {

    private final MemberUtil memberUtil;
    private final BoardDataMapper mapper;

    @Override
    public void check(RequestBoardData form) {
        /**
         * 필수 항목 검증
         *      poster, subject, content
         *      bId, gId - 게시글 등록시, 게시글 수정시에는 변경 X
         *      seq - 게시글 수정시 필수(게시글 번호)
         *          - 게시글 수정시 게시글이 조회 되는지
         *
         *      미로그인 시 필수 - guestPassword - 비회원 비밀번호
         *                                    - 비밀번호 자리수 4자리 이상
         */

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "write" : mode;

        String poster = form.getPoster();
        String subject = form.getSubject();
        String content = form.getContent();
        String bId = form.getBId();
        String gId = form.getGId();
        long seq = form.getSeq();
        String guestPassword = form.getGuestPassword();

        int status = HttpServletResponse.SC_BAD_REQUEST;

        // 공통 필수 항목 체크
        checkRequired(poster, new AlertException("작성자를 입력하세요.", status));
        checkRequired(subject, new AlertException("제목을 입력하세요.", status));
        checkRequired(content, new AlertException("내용을 입력하세요.", status));

        // 등록, 수정 구분 항목 체크
        if (mode.equals("update")) { // 수정
            checkTrue(seq > 0L, new AlertException("잘못된 접근입니다.", status));
            checkTrue(mapper.exists(seq) > 0L, new BoardNotFoundException());

        } else { // 추가
            checkRequired(bId, new AlertException("잘못된 접근입니다.", status));
            checkRequired(gId, new AlertException("잘못된 접근입니다.", status));
        }

        // 미로그인시 체크 항목
        if (!memberUtil.isLogin()) {
            checkRequired(guestPassword, new AlertException("글 수정, 삭제 비밀번호를 입력하세요.", status));
            checkTrue(guestPassword.length() >= 4, new AlertException("비밀번호는 4자리 이상 입력하세요.", status));
        }
    }
}
