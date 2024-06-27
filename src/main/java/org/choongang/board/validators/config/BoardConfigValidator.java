package org.choongang.board.validators.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.RequestBoard;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;

@Component
@RequiredArgsConstructor
public class BoardConfigValidator implements Validator<RequestBoard>, RequiredValidator {

    @Override
    public void check(RequestBoard form) {

        String bId = form.getBId();
        String bName = form.getBName();
        int status = HttpServletResponse.SC_BAD_REQUEST;

        // 필수 항목 검증 - bId(게시판 아이디), bName(게시판 명)
        checkRequired(bId, new AlertException("게시판 아이디를 입력하세요.", status));
        checkRequired(bName, new AlertException("게시판 이름을 입력하세요,", status));

        // 게시판 설정 등록인 경우 - 게시판 중복 여부 체크

    }
}
