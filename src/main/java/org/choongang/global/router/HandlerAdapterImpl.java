package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HandlerAdapterImpl implements HandlerAdapter {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Method method) {
        String m = request.getMethod().toUpperCase(); // 요청 메서드
        String uri = request.getRequestURI();
        Annotation[] annotations = method.getDeclaredAnnotations();

        /*
        for (Annotation anno : annotations) {

        }
        */
        List<Object> args = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            Class cls = param.getType();

            if (cls == HttpServletRequest.class) {
                args.add(request);
            } else if (cls == HttpServletResponse.class) {
                args.add(response);
            } else if (cls == String.class) {
                // 문자열인 경우
                args.add("test");
            } else { // 기타는 setter를 체크해 보고 요청 데이터를 주입
                for (Method _method : cls.getDeclaredMethods()) {
                    String name = _method.getName();
                    if (!name.startsWith("set")) continue;;

                    char[] chars = name.replace("set", "").toCharArray();
                    chars[0] = Character.toLowerCase(chars[0]);
                    name = String.valueOf(chars);
                    String value = request.getParameter(name);
                    if (value == null) continue;

                    Object arg = null;
                    Class clz = _method.getParameterTypes()[0];
                    args.add(arg);
                }
            } // endif
        }

        //Object result = method.invoke()
    }
}
