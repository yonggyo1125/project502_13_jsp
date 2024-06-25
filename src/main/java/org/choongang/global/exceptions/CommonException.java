package org.choongang.global.exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class CommonException extends RuntimeException {
    private int status;

    public CommonException(String message) {
        this(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public CommonException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
