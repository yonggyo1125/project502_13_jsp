package org.choongang.global.config;

import org.choongang.global.config.containers.BeanContainer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxyHandler implements InvocationHandler {

    private final Class clz;
    public MapperProxyHandler(Class clz) {
        this.clz = clz;
    }
    private Object obj;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        BeanContainer bc = BeanContainer.getInstance();

        if (!bc.isLoaded() || obj == null) { // 매 요청 1번만 객체 갱신
            obj = DBConn.getSession().getMapper(clz);
        }

        Object result = method.invoke(obj, args);

        return result;
    }
}
