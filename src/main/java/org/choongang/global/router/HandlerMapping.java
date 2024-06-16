package org.choongang.global.router;

public interface HandlerMapping {
    Object search(Class clazz);
    Object search();
}
