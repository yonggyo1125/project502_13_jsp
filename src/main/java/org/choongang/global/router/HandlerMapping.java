package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object search(Class clazz);
    Object search(HttpServletRequest request);
}
