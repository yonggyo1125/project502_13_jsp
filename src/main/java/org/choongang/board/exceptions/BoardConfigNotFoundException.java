package org.choongang.board.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.exceptions.AlertBackException;

public class BoardConfigNotFoundException extends AlertBackException {
    public BoardConfigNotFoundException() {
        super("등록된 게시판이 아닙니다.", HttpServletResponse.SC_NOT_FOUND);
    }
}
