package org.choongang.global.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.*;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Annotation[] annotations = method.getDeclaredAnnotations();


        /* PathVariable : 경로 변수 패턴 추출 S */
        String[] pathUrls = null;
        List<String> pathVariables = new ArrayList<>();
        for (Annotation anno : annotations) {
            if (m.equals("GET") && anno instanceof GetMapping) {
                GetMapping mapping = (GetMapping) anno;
                pathUrls = mapping.value();
            } else if (m.equals("POST") && anno instanceof PostMapping) {
                PostMapping mapping = (PostMapping) anno;
                pathUrls = mapping.value();
            } else if (m.equals("PATCH") && anno instanceof PatchMapping) {
                PatchMapping mapping = (PatchMapping) anno;
                pathUrls = mapping.value();
            } else if (m.equals("PUT") && anno instanceof PutMapping) {
                PutMapping mapping = (PutMapping) anno;
                pathUrls = mapping.value();
            } else if (m.equals("DELETE") && anno instanceof DeleteMapping) {
                DeleteMapping mapping = (DeleteMapping) anno;
                pathUrls = mapping.value();
            }
        }

        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        for (String url : pathUrls) {
            Matcher matcher = p.matcher(url);
            while (matcher.find()) {
                pathVariables.add(matcher.group(1));
            }
        }
        /* PathVariable : 경로 변수 패턴 추출 E */

        /* 메서드 매개변수 의존성 주입 처리 S */
        List<Object> args = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            try {
                Class cls = param.getType();
                String paramName = null, paramValue = null;
                for (Annotation pa : param.getDeclaredAnnotations()) {
                    if (pa instanceof RequestParam) { // 요청 데이터 매칭
                        RequestParam requestParam = (RequestParam) pa;
                        paramName = requestParam.value();
                        break;
                    } else if (pa instanceof PathVariable) { // 경로 변수 매칭
                        
                    }
                }

                if (paramName != null && !paramName.isBlank()) {
                    paramValue = request.getParameter(paramName);
                    if (cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == double.class || cls == Double.class ||  cls == float.class || cls == Float.class) {
                        paramValue = paramValue == null || paramValue.isBlank()?"0":paramValue;
                    }
                }

                if (cls == HttpServletRequest.class) {
                    args.add(request);
                } else if (cls == HttpServletResponse.class) {
                    args.add(response);
                } else if (cls == int.class) {
                    args.add(Integer.parseInt(paramValue));
                } else if (cls == Integer.class) {
                    args.add(Integer.valueOf(paramValue));
                } else if (cls == long.class) {
                    args.add(Long.parseLong(paramValue));
                } else if (cls == Long.class) {
                    args.add(Long.valueOf(paramValue));
                } else if (cls == float.class) {
                    args.add(Float.parseFloat(paramValue));
                } else if (cls == Float.class) {
                    args.add(Float.valueOf(paramValue));
                } else if (cls == double.class) {
                    args.add(Double.parseDouble(paramValue));
                } else if (cls == Double.class) {
                    args.add(Double.valueOf(paramValue));
                } else if (cls == String.class) {
                    // 문자열인 경우
                    args.add(paramValue);
                } else {
                    // 기타는 setter를 체크해 보고 요청 데이터를 주입
                    // 동적 객체 생성
                    Object paramObj = cls.getDeclaredConstructors()[0].newInstance();
                    for (Method _method : cls.getDeclaredMethods()) {
                        String name = _method.getName();
                        if (!name.startsWith("set")) continue;
                        ;

                        char[] chars = name.replace("set", "").toCharArray();
                        chars[0] = Character.toLowerCase(chars[0]);
                        name = String.valueOf(chars);
                        String value = request.getParameter(name);
                        if (value == null) continue;


                        Class clz = _method.getParameterTypes()[0];
                        // 자료형 변환 후 메서드 호출 처리
                        invokeMethod(paramObj,_method, value, clz);
                    }
                    args.add(paramObj);
                } // endif
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
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
            throw new RuntimeException(e.getMessage());
        }
        /* 요청 메서드 호출 E */
    }

    /**
     * 자료형 변환 후 메서드 호출 처리
     *
     * @param paramObj
     * @param method
     * @param value
     * @param clz
     */
    private void invokeMethod(Object paramObj, Method method, String value, Class clz) {
        try {
            if (clz == String.class) { // 문자열 처리
                method.invoke(paramObj, value);

                /* 기본 자료형 및 Wrapper 클래스 자료형 처리  S */
            } else if (clz == boolean.class) {
                method.invoke(paramObj, Boolean.parseBoolean(value));
            } else if (clz == Boolean.class) {
                method.invoke(paramObj, Boolean.valueOf(value));
            } else if (clz == byte.class) {
                method.invoke(paramObj, Byte.parseByte(value));
            } else if (clz == Byte.class) {
                method.invoke(paramObj, Byte.valueOf(value));
            } else if (clz == short.class) {
                method.invoke(paramObj, Short.parseShort(value));
            } else if (clz == Short.class) {
                method.invoke(paramObj, Short.valueOf(value));
            } else if (clz == int.class) {
                method.invoke(paramObj, Integer.parseInt(value));
            } else if (clz == Integer.class) {
                method.invoke(paramObj, Integer.valueOf(value));
            } else if (clz == long.class) {
                method.invoke(paramObj, Long.parseLong(value));
            } else if (clz == Long.class) {
                method.invoke(paramObj, Long.valueOf(value));
            } else if (clz == float.class) {
                method.invoke(paramObj, Float.parseFloat(value));
            } else if (clz == Float.class) {
                method.invoke(paramObj, Float.valueOf(value));
            } else if (clz == double.class) {
                method.invoke(paramObj, Double.parseDouble(value));
            } else if (clz == Double.class) {
                method.invoke(paramObj, Double.valueOf(value));
                /* 기본 자료형 및 Wrapper 클래스 자료형 처리 E */
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
