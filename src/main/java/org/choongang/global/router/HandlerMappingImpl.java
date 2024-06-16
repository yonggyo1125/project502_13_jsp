package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.*;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Service
public class HandlerMappingImpl implements HandlerMapping{

    @Override
    public Object search(Class clazz) {
        return BeanContainer.getInstance().getBean(clazz);
    }

    //@Override
    public Object search(HttpServletRequest request) {

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        List<Object> items = getControllers();

        for (Object item : items) {
            /** Type 애노테이션에서 체크 S */
            // @RequestMapping, @GetMapping, @PostMapping, @PatchMapping, @PutMapping, @DeleteMapping
            Annotation[] annotations = item.getClass().getDeclaredAnnotations();
            String[] mappings = null;
            String matchUrl = null;
            for (Annotation anno : annotations) {
                if (anno instanceof RequestMapping) { // 모든 요청 방식 매핑
                    RequestMapping mapping = (RequestMapping) anno;
                    mappings = mapping.value();
                } else if (anno instanceof GetMapping && method.equals("GET")) { // GET 방식 매핑
                    GetMapping mapping = (GetMapping) anno;
                    mappings = mapping.value();
                } else if (anno instanceof PostMapping && method.equals("POST")) {
                    PostMapping mapping = (PostMapping) anno;
                    mappings = mapping.value();
                } else if (anno instanceof PutMapping && method.equals("PUT")) {
                    PutMapping mapping = (PutMapping) anno;
                    mappings = mapping.value();
                } else if (anno instanceof PatchMapping && method.equals("PATCH")) {
                    PatchMapping mapping = (PatchMapping) anno;
                    mappings = mapping.value();
                } else if (anno instanceof DeleteMapping && method.equals("DELETE")) {
                    DeleteMapping mapping = (DeleteMapping) anno;
                    mappings = mapping.value();
                }

                if (mappings != null && mappings.length > 0) {
                    matchUrl = Arrays.stream(mappings)
                            .filter(s -> uri.startsWith(request.getContextPath() + s)).toList().get(0);
                    if (matchUrl != null && !matchUrl.isBlank()) {
                        return item;
                    }
                }
            }
            /** Type 애노테이션에서 체크 E */

            /**
             * Method 애노테이션에서 체크 S
             *  - Type 애노테이션 주소 매핑이 되지 않은 경우, 메서드에서 패턴 체크
             */
            for (Method m : item.getClass().getDeclaredMethods()) {
                for (Annotation anno : m.getDeclaredAnnotations()) {
                    mappings = null;
                    if (anno instanceof RequestMapping) { // 모든 요청 방식 매핑
                        RequestMapping mapping = (RequestMapping) anno;
                        mappings = mapping.value();
                    } else if (anno instanceof GetMapping && method.equals("GET")) { // GET 방식 매핑
                        GetMapping mapping = (GetMapping) anno;
                        mappings = mapping.value();
                    } else if (anno instanceof PostMapping && method.equals("POST")) {
                        PostMapping mapping = (PostMapping) anno;
                        mappings = mapping.value();
                    } else if (anno instanceof PutMapping && method.equals("PUT")) {
                        PutMapping mapping = (PutMapping) anno;
                        mappings = mapping.value();
                    } else if (anno instanceof PatchMapping && method.equals("PATCH")) {
                        PatchMapping mapping = (PatchMapping) anno;
                        mappings = mapping.value();
                    } else if (anno instanceof DeleteMapping && method.equals("DELETE")) {
                        DeleteMapping mapping = (DeleteMapping) anno;
                        mappings = mapping.value();
                    }

                    if (mappings != null && mappings.length > 0) {
                        matchUrl = Arrays.stream(mappings)
                                .filter(s -> uri.startsWith(request.getContextPath() + s)).toList().get(0);
                        if (matchUrl != null && !matchUrl.isBlank()) {
                            return item;
                        }
                    }
                }
            }
            /* Method 애노테이션에서 체크 E */
        }

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
