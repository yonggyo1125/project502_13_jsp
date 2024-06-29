package org.choongang.global.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxyHandler implements InvocationHandler {

    private final Class clz;

    public MapperProxyHandler(Class clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj = DBConn.getSession().getMapper(clz);
        Object result = method.invoke(obj, args);
        return result;
    }
}
