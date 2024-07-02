package org.choongang.global.config;

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

        // get으로 시작하는 메서드가 아닌 경우는 sqlSession 객체 갱신
        if (!method.getName().startsWith("get")) {
            //obj = null;
        }

        //if (obj == null) {
            obj = DBConn.getSession().getMapper(clz);
        //}

        Object result = method.invoke(obj, args);

        return result;
    }
}
