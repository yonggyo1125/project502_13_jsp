package org.choongang.file.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileDeleteService;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.PathVariable;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequestMapping("/file/delete")
@RequiredArgsConstructor
public class FileDeleteController {

    private final FileDeleteService deleteService;

    @RequestMapping("/{seq}")
    public String delete(@PathVariable("seq") long seq, HttpServletRequest request) {
        deleteService.delete(seq);

        /**
         * 파일 삭제 후속 처리 - callbackFileDelete 함수가 정의되어 있으면
         *      파일 번호와 함게 호출 - 그 후 처리는 상황에 맞게 처리
         */
        String script = "if (typeof parent.callbackFileDelete === 'function') {" +
                        "   parent.callbackFileDelete(" + seq + "); " +
                        "}";

        request.setAttribute("script", script);
        return "commons/execute_script";
    }
}
