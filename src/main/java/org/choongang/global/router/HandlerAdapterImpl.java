package org.choongang.global.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.Service;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HandlerAdapterImpl implements HandlerAdapter {

    private ObjectMapper om;

    public HandlerAdapterImpl() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data) {

        Object controller = data.get(0); // 컨트롤러 객체
        Method method = (Method)data.get(1); // 찾은 요청 메서드

        String m = request.getMethod().toUpperCase(); // 요청 메서드
        String uri = request.getRequestURI();
        Annotation[] annotations = method.getDeclaredAnnotations();

        /*
        for (Annotation anno : annotations) {

        }
        */
        /* 메서드 매개변수 의존성 주입 처리 S */
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


                    Class clz = _method.getParameterTypes()[0];

                }
                Object arg = null;
                args.add(arg);
            } // endif
        }
        /* 메서드 매개변수 의존성 주입 처리 E */

        /* 요청 메서드 호출 S */
        try {
            Object result = args.isEmpty() ? method.invoke(controller) : method.invoke(controller, args.toArray());

            /**
             *  컨트롤러 타입이 @Controller이면 템플릿 출력,
             * @RestController이면 JSON 문자열로 출력, 응답 헤더를 application/json; charset=UTF-8로 고정
             */
           boolean isRest = Arrays.stream(controller.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof RestController);
           // Rest 컨트롤러인 경우
           if (isRest) {
               response.setContentType("application/json; charset=UTF-8");
               String json = om.writeValueAsString(result);
               PrintWriter out = response.getWriter();
               out.print(json);
               return;
           }

            // 일반 컨트롤러인 경우 문자열 반환값을 템플릿 경로로 사용
            String tpl = "/WEB-INF/templates/" + result + ".jsp";
            RequestDispatcher rd = request.getRequestDispatcher(tpl);
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* 요청 메서드 호출 E */
    }
}
