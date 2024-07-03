package org.choongang.file.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.exceptions.AlertBackException;

public class FileNotFoundException extends AlertBackException {
    public FileNotFoundException() {
        super("파일을 찾을수 없습니다.", HttpServletResponse.SC_NOT_FOUND);
    }
}
