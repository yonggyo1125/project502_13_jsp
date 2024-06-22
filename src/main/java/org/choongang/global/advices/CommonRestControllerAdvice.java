package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.RestControllerAdvice;
import org.choongang.global.exceptions.ExceptionHandler;

@RestControllerAdvice("org.choongang")
public class CommonRestControllerAdvice {

    /**
     * Rest 공통 에러 출력 처리
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletRequest request) {

        return "error";
    }
}
