package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.global.exceptions.ExceptionHandler;

@ControllerAdvice("org.choongang")
public class CommonControllerAdvice {

    @ModelAttribute("commonValue2")
    public String commonValue() {
        return "공통 값 속성 추가 테스트";
    }

    /**
     * 공통 에러 페이지 처리
     * 
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletRequest request) {

        return "errors/error";
    }
}