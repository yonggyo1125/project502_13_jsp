package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.*;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HandlerControllerAdvice {

    private final HttpServletRequest request;

    public boolean handle(Object controller) {
        Class clazz = controller.getClass();
        String pkName = clazz.getPackageName();

        boolean isRest = Arrays.stream(clazz.getAnnotations()).anyMatch(a -> a instanceof RestController);
        List<Object> advices = getControllerAdvices(isRest);
        List<Object> matchedAdvices = new ArrayList<>();
        for (Object advice : advices) {
            Annotation[] annotations = advice.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof ControllerAdvice anno) {
                    boolean isMatched = Arrays.stream(anno.value()).anyMatch(pkName::startsWith);
                    if (isMatched) {
                        matchedAdvices.add(advice);
                    }
                }
            }
        }

        boolean isContinue = true;
        // 매칭된 어드바이스가 있다면
        if (!matchedAdvices.isEmpty()) {
            for (Object matchedAdvice : matchedAdvices) {
                // 인터셉터 체크
                if (matchedAdvice instanceof Interceptor interceptor) {
                    if(!interceptor.preHandle()) {
                        isContinue = false;
                    }
                }

                Method[] methods = matchedAdvice.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    for (Annotation anno : method.getDeclaredAnnotations()) {
                        // 공통 유지할 속성 처리 S
                        if (anno instanceof ModelAttribute ma) {
                            try {
                                String name = ma.value().isBlank() ? method.getName() : ma.value().trim();
                                Object value = method.invoke(matchedAdvice);
                                request.setAttribute(name, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 공통 유지할 속성 처리 E
                    } // endfor
                } // endfor
            }
        }

        return isContinue;
    }

    public List<Object> getControllerAdvices(boolean isRest) {

        return BeanContainer.getInstance()
                    .getBeans()
                    .values()
                    .stream()
                    .filter(b -> Arrays.stream(b.getClass().getAnnotations()).anyMatch(a -> (!isRest && a instanceof ControllerAdvice) || (isRest && a instanceof RestControllerAdvice)))
                    .toList();
    }
}
