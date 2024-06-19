package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface StaticResourceMapping {
    // 정적 경로인지 체크
    boolean check(HttpServletRequest request);
    void route(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
