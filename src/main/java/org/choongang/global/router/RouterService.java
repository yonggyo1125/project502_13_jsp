package org.choongang.global.router;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RouterService {

    private final HandlerMappingImpl handlerMapping;

    /**
     * 컨트롤러 라우팅
     *
     */
    public void route(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Object con = handlerMapping.search(req);
        if (con == null) { // 처리 가능한 컨트롤러를 못찾은 경우 404 응답 코드
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


}
