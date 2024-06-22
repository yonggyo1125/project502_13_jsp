package org.choongang.global.advices;

import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.RestControllerAdvice;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;

import java.util.Arrays;
import java.util.List;

@Service
public class HandlerControllerAdvice {
    public void handle(Object controller) {
        Class clazz = controller.getClass();
        String pkName = clazz.getPackageName();

        boolean isRest = Arrays.stream(clazz.getAnnotations()).anyMatch(a -> a instanceof RestController);
        List<Object> advices = getControllerAdvices(isRest);
        for (Object advice : advices) {
            System.out.println(advice);
        }


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
