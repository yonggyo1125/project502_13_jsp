package org.choongang.board.validators;

import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.global.config.annotations.Component;
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
         */

        String poster = form.getPoster();
        String subject = form.getSubject();
        String content = form.getContent();
        String bId = form.getBId();
        String gId = form.getGId();
        long seq = form.getSeq();
        String guestPassword = form.getGuestPassword();
    }
}
