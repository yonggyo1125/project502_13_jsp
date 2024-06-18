package org.choongang.global.router;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouterService {

    private final HandlerMappingImpl handlerMapping;
    private final HandlerAdapterImpl handlerAdapter;

    /**
     * 컨트롤러 라우팅
     *
     */
    public void route(HttpServletRequest req, HttpServletResponse res) throws IOException {

        List<Object> data = handlerMapping.search(req);
        if (data == null) {

            /**
             *  처리 가능한 컨트롤러를 못찾은 경우 지정된 정적 경로에 파일이 있는지 체크 하고
             *  해당 자원을 파일로 읽어 온 후 파일에 맞는 Content-Type으로 응답 헤더 추가 및 Body쪽에 출력하여 보일 수 있도록 한다.
             *  정적 경로에도 파일을 찾을 수 없다면 404 응답 코드를 내보낸다.
              */

            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 찾은 컨트롤러 요청 메서드를 실행
        handlerAdapter.execute(req, res, data);

    }


}
