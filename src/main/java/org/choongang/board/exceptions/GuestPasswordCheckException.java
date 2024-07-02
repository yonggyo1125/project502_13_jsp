package org.choongang.board.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.exceptions.CommonException;

public class GuestPasswordCheckException extends CommonException {
    public GuestPasswordCheckException() {
        super("비회원 비밀번호 검증이 필요합니다.", HttpServletResponse.SC_UNAUTHORIZED);
    }
}
