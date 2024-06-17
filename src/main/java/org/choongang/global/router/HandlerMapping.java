package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

public interface HandlerMapping {
    Method search(HttpServletRequest request);

}
