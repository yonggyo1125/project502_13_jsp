package org.choongang.global.config;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxyHandler implements InvocationHandler {

    private final Class clz;
    public MapperProxyHandler(Class clz) {
        this.clz = clz;
    }
    private Object obj;
    private SqlSession session = DBConn.getSession();
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        session.clearCache();

        // 매 요청 1번만 객체 갱신
        if (obj == null) {
            obj = session.getMapper(clz);
        }
        Object result = null;
        try {
            result = method.invoke(obj, args);
        } catch (Exception e) {
            session = DBConn.getSession();
            obj = session.getMapper(clz);
            result = method.invoke(obj, args);
        }
        return result;
    }
}
