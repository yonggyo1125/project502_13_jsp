package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HandlerMappingImpl implements HandlerMapping{

    private final HttpServletRequest request;

    @Override
    public Object search(Class clazz) {
        return null;
    }

    @Override
    public Object search() {
        String uri = request.getRequestURI();
        List<Object> items = getControllers();
        items.forEach(System.out::println);
        return null;
    }

    /**
     * 모든 컨트롤러 조회
     *
     * @return
     */
    public List<Object> getControllers() {
       return BeanContainer.getInstance().getBeans().entrySet()
                    .stream()
                    .map(s -> s.getValue())
                .filter(b -> Arrays.stream(b.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof Controller))
                .toList();
    }
}
