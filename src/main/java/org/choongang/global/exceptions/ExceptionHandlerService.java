package org.choongang.global.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.advices.HandlerControllerAdvice;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.RestControllerAdvice;
import org.choongang.global.config.annotations.Service;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionHandlerService {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private final HandlerControllerAdvice handlerAdvice;

    public void handle(Exception e, Object controller) {
        Class clazz = e.getClass();
        Method method = null;
        Object obj = controller;
        boolean isRest = false;
        // 컨트롤러 내부 에러 처리 메서드 조회 S
        for (Method m : controller.getClass().getDeclaredMethods()) {
           ExceptionHandler handler = m.getDeclaredAnnotation(ExceptionHandler.class);
            if (handler != null && Arrays.stream(handler.value()).anyMatch(c -> isSuperClass(c, clazz))) {
                method = m;
                break;
            }
        }
        // 컨트롤러 내부 에러 처리 메서드 조회 E

        if (method == null) {  // 발견하지 못하였다면 ControllerAdvice 또는 RestControllerAdvice에서 찾기
            String pkName = controller.getClass().getPackageName();
            isRest = controller.getClass().getDeclaredAnnotation(RestController.class) != null;
            List<Object> advices = handlerAdvice.getControllerAdvices(isRest);

            for (Object advice : advices) {
                String[] patterns = null;
                if (isRest) {
                    RestControllerAdvice rca = advice.getClass().getDeclaredAnnotation(RestControllerAdvice.class);
                    patterns = rca.value();
                } else {
                    ControllerAdvice ca = advice.getClass().getDeclaredAnnotation(ControllerAdvice.class);
                    patterns = ca.value();
                }

                if (patterns != null && Arrays.stream(patterns).anyMatch(pkName::startsWith)) {
                    for (Method m : advice.getClass().getDeclaredMethods()) {
                        ExceptionHandler handler = m.getDeclaredAnnotation(ExceptionHandler.class);
                        if (handler != null && Arrays.stream(handler.value()).anyMatch(c -> isSuperClass(c, clazz))) {
                            method = m;
                            obj = advice;
                            break;
                        }
                    }
                }
            } // endfor

        } // endif

        if (method == null) {
            throw new RuntimeException(e);
        }

        List<Object> args = new ArrayList<>();
        for (Class c : method.getParameterTypes()) {
            if (isSuperClass(c, clazz)) {
                args.add(e);
            } else if (c == HttpServletRequest.class) {
                args.add(request);
            } else if (c == HttpServletResponse.class) {
                args.add(response);
            } else if (c == HttpSession.class) {
                args.add(session);
            }
        }

        try {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("exception", e);

            Object result = method.invoke(obj, args.toArray());

            isRest = isRest ? isRest : obj.getClass().getDeclaredAnnotation(RestController.class) != null;
            if (!isRest) { // Rest 방식이 아닌 경우 템플릿 출력
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/templates/" + result + ".jsp");
                rd.forward(request, response);
                return;
            }

            // Rest 방식인 경우 JSON 형식으로 출력 처리
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(om.writeValueAsString(result));

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private boolean isSuperClass(Class clz, Class target) {
        if (clz == target) {
            return true;
        }

        Class superTarget = target.getSuperclass();
        if (superTarget == null) {
            return false;
        }

        return isSuperClass(clz, superTarget);
    }
}
