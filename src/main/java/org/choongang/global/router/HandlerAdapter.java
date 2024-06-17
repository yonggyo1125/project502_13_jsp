package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public interface HandlerAdapter {
    void execute(HttpServletRequest request, HttpServletResponse response, Method method);
}
