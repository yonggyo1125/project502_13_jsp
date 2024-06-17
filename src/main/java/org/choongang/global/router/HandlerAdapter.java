package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface HandlerAdapter {
    void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data);
}
