package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;

public interface HandlerMapping {
    Object search(Class clazz);
    Object search(HttpServletRequest request);
    void setMethod(Method method);
    Method getMethod();
}
