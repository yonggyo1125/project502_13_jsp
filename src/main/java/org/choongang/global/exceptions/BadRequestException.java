package org.choongang.global.exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends CommonException {
    public BadRequestException() {
        this("잘못된 요청입니다.");
    }

    public BadRequestException(String message) {
        super(message, HttpServletResponse.SC_BAD_REQUEST); // 응답 코드 400으로 제한
    }
}
