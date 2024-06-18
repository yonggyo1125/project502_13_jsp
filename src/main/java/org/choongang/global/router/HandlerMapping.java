package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface HandlerMapping {
    List<Object> search(HttpServletRequest request);

}
