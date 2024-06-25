package org.choongang.global.exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class UnAuthorizedException extends CommonException {
    public UnAuthorizedException() {
        super("접근 권한이 없습니다.", HttpServletResponse.SC_UNAUTHORIZED); // 401
    }
}
