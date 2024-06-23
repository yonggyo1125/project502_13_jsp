
# 목차
1. [사이트 기본 구성](https://github.com/yonggyo1125/project501_13_jsp?tab=readme-ov-file#%EC%B4%88%EA%B8%B0-%EC%84%A4%EC%A0%95)
2. [게시판 만들기](https://github.com/yonggyo1125/project501_13_jsp/tree/board)
3. [포켓몬 도감 가이드](https://github.com/yonggyo1125/project501_13_jsp/tree/pokemon)

---
# 초기 설정
## 의존성 추가 
- servlet-api, servlet.jsp-api, jstl, lombok, ojdbc11, mybatis, slf4j-api, logback-classic, jbcrypt, jackson databind, mockito, javafaker 등

> build.gradle

```groovy
...

dependencies {
    compileOnly 'jakarta.servlet:jakarta.servlet-api:6.0.0'
    compileOnly 'jakarta.servlet.jsp:jakarta.servlet.jsp-api:3.1.1'
    implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0'
    implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1'
    compileOnly 'org.projectlombok:lombok:1.18.32'
    annotationProcessor 'org.projectlombok:lombok:1.18.32'
    runtimeOnly 'com.oracle.database.jdbc:ojdbc11:23.4.0.24.05'
    implementation 'org.mybatis:mybatis:3.5.16'
    implementation 'org.mindrot:jbcrypt:0.4'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.17.1'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1'
    implementation 'org.slf4j:slf4j-api:2.0.13'
    implementation 'ch.qos.logback:logback-classic:1.5.6'
    implementation 'com.github.javafaker:javafaker:1.0.2'

    testImplementation 'org.mockito:mockito-core:5.12.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:5.12.0'
    testImplementation platform('org.junit:junit-bom:5.10.2')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}

...

```

## MVC 기본 경로 생성 

- 템플릿 경로 생성 : src/main/webapp/WEB-INF/templates
- 사이트 전체 설정 경로 : src/main/java/org/choongang/global/config 
- 라우터 설정 경로 : src/main/java/org/choongang/global/router

## 웹 설정 애노테이션
- 기준 패키지 : src/main/java/org/choongang/global/config/annotations

- @Component, @Controller, @RestController, @Service를 클래스명 위에 적용하면 BeanContainer가 자동으로 스캔하여 싱글톤 형태로 객체를 자동 생성합니다.
- 해당 클래스의 생성자 매개변수로 의존하는 객체를 정의한다면 BeanContainer에서 생성된 관리 객체라면 자동 주입을 합니다.
### Component.java 

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
```

### Controller.java

> 일반적인 요청과 응답을 처리하는 컨트롤러
> 각 요청 메서드의 반환값은 String이며 이 문자열은 템플릿 경로가 됩니다. 
> 예를 들어 반환값이 member/join 이라면 webapp/WEB-INF/templates/member/join.jsp를 찾고 해당 뷰를 RequestDispatcher를 통해서 버퍼에 추가하고 출력 합니다.

```java
package org.choongang.global.config.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
```

### RestController.java

> Rest 방식의 응답을 처리하는 컨트롤러, 반환값은 자바 객체, 문자열 등이 될 수 있으며 JSON 형식으로 변환하여 출력합니다.

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
}
```

### Service.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
```

### 요청 주소 매핑 관련 애노테이션 
- 하기 요청 매핑은 클래스명 위와 메서드에 각각 적용 가능 합니다. 클래스명 위에 적용하면 하위 요청 메서드의 공통 접두어(prefix)로 동작합니다. 
  - @RequestMapping : 요청방식과 상관없이 모든 요청에 매핑
  - @GetMapping: GET GET 방식의 요청에 매핑 
  - @PostMapping: POST 방식의 요청에 매핑
  - @PatchMapping: PATCH 방식의 요청에 매핑
  - @PutMapping: PUT 방식의 요청에 매핑 
  - @DeleteMapping: DELETE 방식의 요청에 매핑
- 사용예

```java
@Controller
@RequestMapping("/member")
public class MemberController {
    ...

    @GetMapping("/join")
    public String join(RequestJoin form) {
        ...
    }
}
```

#### RequestMapping.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String[] value() default "";
}
```

#### GetMapping.java

```java
package org.choongang.global.config.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String[] value() default "";
}
```

#### PostMapping.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {
    String[] value() default "";
}
```

#### PatchMapping.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatchMapping {
    String[] value() default "";
}
```

#### PutMapping.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PutMapping {
    String[] value() default "";
}
```

#### DeleteMapping.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteMapping {
    String[] value() default "";
}
```

### 요청 데이터 매핑 관련 애노테이션
- 기본적으로 컨트롤러의 요청 메서드의 매개변수로 Getter, Setter가 정의된 데이터 클래스가 지정되어 있다면 패턴 이름과 동일한 요청 데이터(GET, POST 등 모든 요청 방식)의 이름으로 찾아 객체를 완성하고 주입해 줍니다.
- 다만 단일 자료형 매개변수 예) String mode, int seq와 같은 형태일때는 매칭되는 요청 데이터 이름을 명시하면 찾아서 주입해 줍니다.
- @RequestParam : 요청 데이터(GET, POST 등 모든 요청)의 이름을 명시하면 찾아서 해당 변수에 주입해 줍니다.
- @PathVariable : 경로 변수와 같은 경로상에 변경되는 데이터를 패턴화하여 명시하면 찾아서 주입해 줍니다.
- 사용예

```java
@GetMapping("/{mode}")
public String join(@PathVariable("mode") String mode, @RequestParam("seq") int seq, ...) {
    
    ...
    
}
```

#### RequestParam.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value();
}
```

#### PathVariable.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {
    String value();
}
```

### 날짜 형식화 관련 
> LocalDate, LocalTime, LocalDateTime 자료형으로 선언된 멤버변수를 가진 데이터 클래스가 요청 매서드의 매개변수로 정의된 경우 유입된 문자열을 해당 객체로 변환할 필요가 있습니다. 이때 유입된 문자열의 형식을 명시하고 DateTimeFormatter를 통해서 변환하여야 하는데 이를 @DateTimeFormat을 통해서 명시하면 그 패턴으로 문자열 -> LocalDate, LocalTime, LocalDateTime과 같은 객체로 변환합니다. 

- 사용 예)

```java 
...
@Data
public class RequestJoin {
    ...

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt;
}

```

```java
@Controller
@RequestMapping("/member")
public class MemberController {
    private final JoinService joinService;

    @GetMapping("/join")
    public String join(RequestJoin form) {
        
        ...
        
        return "member/join";
    }
}
```

#### DateTimeFormat.java 

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormat {
    String value();
}
```


## 객체 컨테이너
> @Controller, @RestController, @Component, @Service와 같은 애노테이션이 붙어 있는 클래스를 찾아서 객체를 싱글톤 패턴으로 생성하고 관리 합니다. 해당 클래스의 생성자 매개변수에 정의된 의존 객체가 있다면 재귀적으로 찾아서 모두 생성하고 주입해 줍니다. 
> 또한 서블릿 기본 객체 중 HttpServletRequest, HttpServletResponse 객체 역시 DispatcherServlet으로 유입되었을때 함께 주입시 사용하기 위해 객체 컨테이너에 포함시킵니다.

### org/choongang/global/config/containers/BeanContainer.java 

```java
package org.choongang.global.config.containers;

import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.Service;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanContainer {
    private static BeanContainer instance;

    private Map<String, Object> beans;


    public BeanContainer() {
        beans = new HashMap<>();
    }

    public void loadBeans() {
        // 패키지 경로 기준으로 스캔 파일 경로 조회
        try {
            String rootPath = new File(getClass().getResource("../../../").getPath()).getCanonicalPath();
            String packageName = getClass().getPackageName().replace(".global.config.containers", "");
            List<Class> classNames = getClassNames(rootPath, packageName);

            for (Class clazz : classNames) {
                // 인터페이스는 동적 객체 생성을 하지 않으므로 건너띄기
                if (clazz.isInterface()) {
                    continue;
                }

                // 애노테이션 중 Controller, RestController, Component, Service 등이 TYPE 애노테이션으로 정의된 경우 beans 컨테이너에 객체 생성하여 보관
                // 키값은 전체 클래스명, 값은 생성된 객체
                String key = clazz.getName();

                // 이미 생성된 객체라면 생성된 객체로 활용
                if (beans.containsKey(key)) continue;;


                Annotation[] annotations = clazz.getDeclaredAnnotations();

                boolean isBean = false;
                for (Annotation anno : annotations) {
                    if (anno instanceof Controller || anno instanceof RestController || anno instanceof Service || anno instanceof Component)  {
                        isBean = true;
                        break;
                    }
                }
                // 컨테이너가 관리할 객체라면 생성자 매개변수의 의존성을 체크하고 의존성이 있다면 해당 객체를 생성하고 의존성을 해결한다.
                if (isBean) {
                    Constructor con = clazz.getDeclaredConstructors()[0];
                    List<Object> objs = resolveDependencies(key, con);
                    if (!beans.containsKey(key)) {
                        Object obj = con.getParameterTypes().length == 0 ? con.newInstance() : con.newInstance(objs.toArray());
                        beans.put(key, obj);
                    }
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BeanContainer getInstance() {
        if (instance == null) {
            instance = new BeanContainer();
        }

        return instance;
    }

    /**
     * 생성된 객체 조회
     *
     * @param clazz
     * @return
     */
    public <T> T getBean(Class clazz) {
        return (T)beans.get(clazz.getName());
    }

    public void addBean(Object obj) {

        beans.put(obj.getClass().getName(), obj);
    }

    public void addBean(String key, Object obj) {
        beans.put(key, obj);
    }

    // 전체 컨테이너 객체 반환
    public Map<String, Object> getBeans() {
        return beans;
    }

    /**
     * 의존성의 의존성을 재귀적으로 체크하여 필요한 의존성의 객체를 모두 생성한다.
     *
     * @param con
     */
    private List<Object> resolveDependencies(String key, Constructor con) throws Exception {
        List<Object> dependencies = new ArrayList<>();
        if (beans.containsKey(key)) {
            dependencies.add(beans.get(key));
            return dependencies;
        }

        Class[] parameters = con.getParameterTypes();
        if (parameters.length == 0) {
            Object obj = con.newInstance();
            dependencies.add(obj);
        } else {
            for(Class clazz : parameters) {

                Object obj = beans.get(clazz.getName());
                if (obj == null) {
                    Constructor _con = clazz.getDeclaredConstructors()[0];

                    if (_con.getParameterTypes().length == 0) {
                        obj = _con.newInstance();
                    } else {
                        List<Object> deps = resolveDependencies(clazz.getName(), _con);
                        obj = _con.newInstance(deps.toArray());
                    }
                }
                dependencies.add(obj);
            }
        }


        return dependencies;
    }

    private List<Class> getClassNames(String rootPath, String packageName) {
        List<Class> classes = new ArrayList<>();
        List<File> files = getFiles(rootPath);
        for (File file : files) {
            String path = file.getAbsolutePath();
            String className = packageName + "." + path.replace(rootPath + File.separator, "").replace(".class", "").replace(File.separator, ".");
            try {
                Class cls = Class.forName(className);
                classes.add(cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private List<File> getFiles(String rootPath) {
        List<File> items = new ArrayList<>();
        File[] files = new File(rootPath).listFiles();
        if (files == null) return items;

        for (File file : files) {
            if (file.isDirectory()) {
                List<File> _files = getFiles(file.getAbsolutePath());
                if (!_files.isEmpty()) items.addAll(_files);
            } else {
                items.add(file);
            }
        }
        return items;
    }
}
```

## 라우팅 처리 

> 모든 요청은 단일 창구로 DispatcherServlet으로 유입이 되며 유입된 요청을 RouterService가 분석하여 BeanContainer에 등록된 컨트롤러 객체 및 요청 메서드를 HandlerMapping 구현체가 이를 찾아주게 됩니다. 찾아준 컨트롤러 객체의 요청 메서드는 HandlerAdapter 구현 객체가 실행하며 실행시에 요청 메서드에 정의된 매개변수에 따라 요청 데이터를 분석하여 이를 변수 또는 Data 클래스 형태의 객체에 알맞게 주입해 줍니다. 또한 서블릿 기본 객체인 HttpServletRequest request, HttpServletResponse response와 같은 객체를 주입한다면 이 역시 BeanContainer에서 찾아서 완성된 객체를 주입해 줍니다.

- 기본 경로 : src/main/java/org/choongang/global/router/

### HandlerMapping.java 

```java
package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface HandlerMapping {
    List<Object> search(HttpServletRequest request);

}
```

### HandlerMappingImpl.java 

```java
package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.*;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HandlerMappingImpl implements HandlerMapping{


    private String controllerUrl;

    @Override
    public List<Object> search(HttpServletRequest request) {

        List<Object> items = getControllers();

        for (Object item : items) {
            /** Type 애노테이션에서 체크 S */
            // @RequestMapping, @GetMapping, @PostMapping, @PatchMapping, @PutMapping, @DeleteMapping
            if (isMatch(request,item.getClass().getDeclaredAnnotations(), false, null)) {
                // 메서드 체크
                for (Method m : item.getClass().getDeclaredMethods()) {
                    if (isMatch(request, m.getDeclaredAnnotations(), true, controllerUrl)) {
                        return List.of(item, m);
                    }
                }
            }
            /** Type 애노테이션에서 체크 E */

            /**
             * Method 애노테이션에서 체크 S
             *  - Type 애노테이션 주소 매핑이 되지 않은 경우, 메서드에서 패턴 체크
             */
            for (Method m : item.getClass().getDeclaredMethods()) {
                if (isMatch(request, m.getDeclaredAnnotations(), true, null)) {
                    return List.of(item, m);
                }
            }
            /* Method 애노테이션에서 체크 E */
        }

        return null;
    }


    /**
     *
     * @param request
     * @param annotations : 적용 애노테이션 목록
     * @param isMethod : 메서드의 에노테이션 체크인지
     * @param prefixUrl : 컨트롤러 체크인 경우 타입 애노테이션에서 적용된 경우
     * @return
     */
    private boolean isMatch(HttpServletRequest request, Annotation[] annotations, boolean isMethod, String prefixUrl) {

        String uri = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String[] mappings = null;
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

                String matchUrl = null;
                if (isMethod) {
                    String addUrl = prefixUrl == null ? "" : prefixUrl;
                    // 메서드인 경우 *와 {경로변수} 고려하여 처리
                    for(String mapping : mappings) {
                        String pattern = mapping.replace("/*", "/\\w*")
                                .replaceAll("/\\{\\w+\\}", "/(\\\\w*)");

                        Pattern p = Pattern.compile("^" + request.getContextPath() + addUrl + pattern + "$");
                        Matcher matcher = p.matcher(uri);
                        return matcher.find();
                    }
                } else {
                    List<String> matches = Arrays.stream(mappings)
                            .filter(s -> uri.startsWith(request.getContextPath() + s)).toList();
                    if (!matches.isEmpty()) {
                        matchUrl = matches.get(0);
                        controllerUrl = matchUrl;
                    }
                }
                return matchUrl != null && !matchUrl.isBlank();
            }
        }

        return false;
    }

    /**
     * 모든 컨트롤러 조회
     *
     * @return
     */
    private List<Object> getControllers() {
       return BeanContainer.getInstance().getBeans().entrySet()
                    .stream()
                    .map(s -> s.getValue())
                .filter(b -> Arrays.stream(b.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof Controller || a instanceof RestController))
                .toList();
    }
}
```

### HandlerAdapter.java 

```java 
package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface HandlerAdapter {
    void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data);
}
```

### HandlerAdapterImpl.java

```java
package org.choongang.global.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.*;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HandlerAdapterImpl implements HandlerAdapter {

    private final ObjectMapper om;

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

        /* 컨트롤러 애노테이션 처리 S */
        String[] rootUrls = {""};
        for (Annotation anno : controller.getClass().getDeclaredAnnotations()) {
            rootUrls = getMappingUrl(m, anno);
        }
        /* 컨트롤러 애노테이션 처리 E */

        /* PathVariable : 경로 변수 패턴 값 추출  S */
        String[] pathUrls = {""};
        Map<String, String> pathVariables = new HashMap<>();
        for (Annotation anno : annotations) {
            pathUrls = getMappingUrl(m, anno);
        }

        if (pathUrls != null) {
            Pattern p = Pattern.compile("\\{(\\w+)\\}");
            for (String url : pathUrls) {
                Matcher matcher = p.matcher(url);

                List<String> matched = new ArrayList<>();
                while (matcher.find()) {
                    matched.add(matcher.group(1));
                }

                if (matched.isEmpty()) continue;;

                for (String rUrl : rootUrls) {
                    String _url = request.getContextPath() + rUrl + url;
                    for (String s : matched) {
                        _url = _url.replace("{" + s + "}", "(\\w*)");
                    }

                    Pattern p2 = Pattern.compile("^" + _url+"$");
                    Matcher matcher2 = p2.matcher(request.getRequestURI());
                    while (matcher2.find()) {
                        for (int i = 0; i < matched.size(); i++) {
                            pathVariables.put(matched.get(i), matcher2.group(i + 1));
                        }
                    }
                }
            }
        }

        /* PathVariable : 경로 변수 패턴 값 추출 E */

        /* 메서드 매개변수 의존성 주입 처리 S */
        List<Object> args = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            try {
                Class cls = param.getType();
                String paramValue = null;
                for (Annotation pa : param.getDeclaredAnnotations()) {
                    if (pa instanceof RequestParam requestParam) { // 요청 데이터 매칭
                        String paramName = requestParam.value();
                        paramValue = request.getParameter(paramName);
                        break;
                    } else if (pa instanceof PathVariable pathVariable) { // 경로 변수 매칭
                        String pathName = pathVariable.value();
                        paramValue = pathVariables.get(pathName);
                        break;
                    }
                }

                if (cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == double.class || cls == Double.class ||  cls == float.class || cls == Float.class) {
                    paramValue = paramValue == null || paramValue.isBlank()?"0":paramValue;
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

                        char[] chars = name.replace("set", "").toCharArray();
                        chars[0] = Character.toLowerCase(chars[0]);
                        name = String.valueOf(chars);
                        String value = request.getParameter(name);
                        if (value == null) continue;


                        Class clz = _method.getParameterTypes()[0];
                        // 자료형 변환 후 메서드 호출 처리
                        invokeMethod(paramObj,_method, value, clz, name);
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
     * @param fieldNm - 멤버변수명
     */
    private void invokeMethod(Object paramObj, Method method, String value, Class clz, String fieldNm) {
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
                // LocalDate, LocalTime, LocalDateTime 자료형 처리 S
            } else if (clz == LocalDateTime.class || clz == LocalDate.class || clz == LocalTime.class) {
               Field field = paramObj.getClass().getDeclaredField(fieldNm);
               for (Annotation a : field.getDeclaredAnnotations()) {
                   if (a instanceof DateTimeFormat dateTimeFormat) {
                       String pattern = dateTimeFormat.value();
                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                       if (clz == LocalTime.class) {
                           method.invoke(paramObj, LocalTime.parse(value, formatter));
                       } else if (clz == LocalDate.class) {
                           method.invoke(paramObj, LocalDate.parse(value, formatter));
                       } else {
                           method.invoke(paramObj, LocalDateTime.parse(value, formatter));
                       }
                       break;
                   } // endif
               } // endfor
                // LocalDate, LocalTime, LocalDateTime 자료형 처리 E
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 요청 메서드 & 애노테이션으로 설정된 mapping Url 조회
     *
     * @param method
     * @param anno
     * @return
     */
    private String[] getMappingUrl(String method, Annotation anno) {

        // RequestMapping은 모든 요청에 해당하므로 정의되어 있다면 이 설정으로 교체하고 반환한다.
        if (anno instanceof  RequestMapping) {
            RequestMapping mapping = (RequestMapping) anno;
            return mapping.value();
        }

        if (method.equals("GET") && anno instanceof GetMapping) {
            GetMapping mapping = (GetMapping) anno;
            return mapping.value();
        } else if (method.equals("POST") && anno instanceof PostMapping) {
            PostMapping mapping = (PostMapping) anno;
            return mapping.value();
        } else if (method.equals("PATCH") && anno instanceof PatchMapping) {
            PatchMapping mapping = (PatchMapping) anno;
            return mapping.value();
        } else if (method.equals("PUT") && anno instanceof PutMapping) {
            PutMapping mapping = (PutMapping) anno;
            return mapping.value();
        } else if (method.equals("DELETE") && anno instanceof DeleteMapping) {
            DeleteMapping mapping = (DeleteMapping) anno;
            return mapping.value();
        }

        return null;
    }
}
```

### RouterService.java

```java
package org.choongang.global.router;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouterService {

    private final HandlerMappingImpl handlerMapping;
    private final HandlerAdapterImpl handlerAdapter;

    /**
     * 컨트롤러 라우팅
     *
     */
    public void route(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<Object> data = handlerMapping.search(req);
        if (data == null) { // 처리 가능한 컨트롤러를 못찾은 경우 404 응답 코드
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 찾은 컨트롤러 요청 메서드를 실행
        handlerAdapter.execute(req, res, data);

    }


}
```

### DispatcherServlet.java

```java
package org.choongang.global.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        BeanContainer bc = BeanContainer.getInstance();
        bc.addBean(HttpServletRequest.class.getName(), request);
        bc.addBean(HttpServletResponse.class.getName(), response);

        bc.loadBeans();

        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
    }
}
```

## 구현내용 적용해 보기

> member 도메인을 만들고 다음과 같이 구성해 봅니다.
> 구성이 완료되면 브라우저 주소창에 /컨텍스트 경로/member/mode/test/123?email=test@test.org&password=1234&regDt=2024-06-19 11:22:30 과 같이 입력해 본 후 정상적으로 값이 콘솔에 출력되는지 테스트 합니다. 
> 또한 템플릿 webapp/WEB-INF/templates/member/join.jsp로 정상 출력되는지 테스트 합니다.


### org/choongang/member/controllers/MemberController.java

```java
package org.choongang.member.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.*;
import org.choongang.member.services.JoinService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    
    @GetMapping("/{mode}/test/{num}")
    public String join(@PathVariable("mode") String mode, @RequestParam("seq") int seq, RequestJoin form, HttpServletRequest request, HttpServletResponse response, @PathVariable("num") int num) {
        
        System.out.printf("mode=%s, seq=%d, num=%d%n", mode, seq, num);
        System.out.println(form);
        
        return "member/join";
    }
}
```

### org/choongang/member/controllers/RequestJoin.java

```java
package org.choongang.member.controllers;

import lombok.Data;
import org.choongang.global.config.annotations.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class RequestJoin {
    private String email;
    private String password;
    private String confirmPassword;
    private boolean termsAgree;
    private byte num1;
    private short num2;
    private int num3;
    private long num4;
    private float num5;
    private double num6;
    private Byte num11;
    private Short num22;
    private Integer num33;
    private Long num44;
    private Float num55;
    private Double num66;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt;
}
```

### webapp/WEB-INF/templates/member/join.jsp

```jsp
<%@page contentType="text/html; charset=UTF-8" %>
템플릿 출력
```

# [레이아웃 구성](https://github.com/yonggyo1125/project501_13_jsp/tree/layout) 
> 레이아웃은 커스텀 태그를 활용하여 구성합니다.
> 공통 레이아웃(tags/layouts/common.tag) 구성은 모든 레이아웃의 기본이 될수 있는 구성이며 다른 레이아웃(예 - tags/layouts/main.tag)이 공통 레이아웃을 바탕으로 2차 구성을 하게 됩니다.
> 화면 구성시에 각 출력 문구들은 메세지 파일을 통해 관리 됩니다. 메세지 파일은 역할별로 구분하여 commons.properties는 공통 문구, validations.properties는 유효성 검사시 문구, errors.properties는 에러 관련 문구로 분리합니다.

## 메세지 파일 구성
- src/main/resources/messages/commons.properties
- src/main/resources/messages/validations.properties
- src/main/resources/messages/errors.properties

### commons.properties

```properties
# 사이트 공통
SITE_TITLE=중앙정보처리학원
```

## 레이아웃 파일 구성 
- 기준 경로 : webapp/WEB-INF/tags/layouts/

### common.tag

```jsp
<%@ tag body-content="scriptless" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="footer" fragment="true" %>
<%@ attribute name="commonCss" fragment="true" %>
<%@ attribute name="commonJs" fragment="true" %>
<%@ attribute name="title" %>
<fmt:setBundle basename="messages.commons" />
<c:url var="cssUrl" value="/css/" />
<c:url var="jsUrl" value="/js/" />
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>
         <c:if test="${!empty title}">
         ${title} -
         </c:if>
         <fmt:message key="SITE_TITLE" />
        </title>
        <link rel="stylesheet" type="text/css" href="${cssUrl}style.css">
        <jsp:invoke fragment="commonCss" />
        <c:if test="${addCss != null}">
            <c:forEach var="cssFile" items="${addCss}">
                <link rel="stylesheet" type="text/css" href="${cssUrl}${cssFile}.css">
            </c:forEach>
        </c:if>

        <script src="${jsUrl}common.js"></script>
        <jsp:invoke fragment="commonJs" />
        <c:if test="${addScript != null}">
            <c:forEach var="jsFile" items="${addScript}">
                <script src="${jsUrl}${jsFile}.js"></script>
            </c:forEach>
        </c:if>
    </head>
    <body>
        <header>
            <jsp:invoke fragment="header" />
        </header>
        <main>
            <jsp:doBody />
        </main>
        <footer>
            <jsp:invoke fragment="footer" />
        </footer>
    </body>
    <iframe name="ifrmProcess" class="dn"></iframe>
</html>
```

### main.tag

```jsp
<%@ tag body-content="scriptless" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" %>
<c:url var="cssUrl" value="/css/" />
<c:url var="jsUrl" value="/js/" />

<layout:common title="${title}">
    <jsp:attribute name="header">
        <h1>메인 레이아웃 상단 영역!</h1>
    </jsp:attribute>
    <jsp:attribute name="footer">
        <h1>메인 레이아웃 하단 영역!</h1>
    </jsp:attribute>
    <jsp:attribute name="commonCss">
        <link rel="stylesheet" type="text/css" href="${cssUrl}main.css">
    </jsp:attribute>
    <jsp:attribute name="commonJs">
        <script src="${jsUrl}main.js"></script>
    </jsp:attribute>
    <jsp:body>
        <jsp:doBody />
    </jsp:body>
</layout:common>
```

## 공통 CSS, JS 추가 

###  webapp/static/css/style.css

```css
.dn { display: none !important; }
```

###  webapp/static/js/common.js


## 적용해보기

> 지난 기본 구성시 생성한 ... WEB-INF/templates/member/join.jsp에 다음과 같이 적용해봅니다. 정상적으로 반영되는지 확인합니다. 

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main title="회원가입">
    <h1>회원가입</h1>
</layout:main>
```

## css, js, images이 있는 webapp/static 쪽 정적 경로 처리 
> DispatcherServlet에 service 메서드는 모든 요청에 다 유입이 되므로 css, js 파일도 상관없이 모두 여기를 거쳐 가게 된다. 서블릿을 통한 요청 처리로 유입이 되므로 정상적으로 css, js로 처리가 되지 않는다. 따라서 직접 읽어 올수 있도록 정적 경로 분리 처리를 해야 한다. 


### 웹 정적 경로 처리 
- 정적 경로는 2가지 형태로 구현한다.
  - webapp/static : 요청 URL에 해당하는 컨트롤러 및 요청 메서드를 찾지 못한 경우는 이 경로에서 정적인 자원을 찾는다. 가령 /css/style.css의 경로가 컨트롤러에 없는데 해당 자원이 webapp/static/css/style.css가 있다면 이 파일의 내용물을 출력한다. 
  - 파일 업로드와 같은 설정 파일에서 유입되는 경우 - 설정 파일 처리 쪽에서 자세하게 설명
- StaticResourceMapping의 check 메서드는 현재 요청 URL이 컨트롤러가 아닌 정적 경로에 해당하는지 체크 합니다. 즉 webapp/static에 존재하는지, 또는 별도 설정 파일 경로에 파일이 있는지를 체크 합니다.
- StaticResourceMapping의 route 메서드는 정적 경로가 발견되었다면 해당 경로의 파일을 읽어 파일 형식(content-type)을 응답 헤더에 출력하고, 바디 쪽에 파일 내용을 출력하여 웹 화면에 보여줍니다.

### org/choongang/router/StaticResourceMapping.java

```java
package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface StaticResourceMapping {
    // 정적 경로인지 체크
    boolean check(HttpServletRequest request);
    void route(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
```

### org/choongang/router/StaticResourceMappingImpl.java

```java
package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StaticResourceMappingImpl implements StaticResourceMapping {

    /**
     * 정적 자원 경로인지 체크
     *
     * @param request
     * @return
     */
    @Override
    public boolean check(HttpServletRequest request) {

        // webapp/static 경로 유무 체크
        return getStaticPath(request).exists();

    }

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // webapp/static 경로 처리 S
        File file = getStaticPath(request);
        if (file.exists()) {
            Path source = file.toPath();
            String contentType = Files.probeContentType(source);
            response.setContentType(contentType);

            OutputStream out = response.getOutputStream();

            InputStream in = new BufferedInputStream(new FileInputStream(file));
            out.write(in.readAllBytes());
            return;
        }
        // webapp/static 경로 처리 E
    }

    private File getStaticPath(HttpServletRequest request) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        String path = request.getServletContext().getRealPath("/static");
        File file = new File(path + uri);

        return file;
    }
}
```


### org/choongang/router/RouterService.java

- 정적 라우팅을 다음과 같이 반영합니다.

```java

...

@Service
@RequiredArgsConstructor
public class RouterService {

    private final HandlerMappingImpl handlerMapping;
    private final HandlerAdapterImpl handlerAdapter;
    private final StaticResourceMappingImpl staticResourceMapping;

    /**
     * 컨트롤러 라우팅
     *
     */
    public void route(HttpServletRequest req, HttpServletResponse res) throws IOException {

        List<Object> data = handlerMapping.search(req);
        if (data == null) {

            /**
             *  처리 가능한 컨트롤러를 못찾은 경우 지정된 정적 경로에 파일이 있는지 체크 하고
             *  해당 자원을 파일로 읽어 온 후 파일에 맞는 Content-Type으로 응답 헤더 추가 및 Body쪽에 출력하여 보일 수 있도록 한다.
             *  정적 경로에도 파일을 찾을 수 없다면 404 응답 코드를 내보낸다.
             */

            // 정적 자원이라면 정적 라우팅 처리
            if (staticResourceMapping.check(req)) {
                staticResourceMapping.route(req, res);
                return;
            }

            // 컨트롤러도 발견하지 못하고 정적 라우팅도 아니라면 404
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 찾은 컨트롤러 요청 메서드를 실행
        handlerAdapter.execute(req, res, data);

    }
}
```

> /컨택스트 경로/css/style.css, /컨택스트 경로/js/common.js가 정상적으로 접속이 된다면 반영 성공  


# 설정 파일 구성

> 설정 파일을 resources/application.properties, resources/application-prod.properties 생성합니다. 이번에 구성할 부분은 파일 업로드 경로와 URL을 설정합니다. 파일이 저장될 위치는 개발 환경에 따라 또는 배포하는 서버에 따라 달라질 수 있습니다. 이를 대응하기 위한 목적으로 환경에 따라 달라질 수 있는 값을 설정합니다.
> application-prod.properties에서 prod는 환경 변수로 톰캣에서 실행시 설정하는 환경 변수로 prod값으로 설정하면 배포 서버의 설정으로 볼수 있습니다.


## [설정 파일](https://github.com/yonggyo1125/project501_13_jsp/tree/config_file) 
- application.properties는 개발 환경에 필요한 설정 파일
- application-prod.properties는 배포 환경에 필요한 설정 파일

### src/main/resources/application.properties

```properties
# 파일 업로드 경로
file.upload.path=D:/uploads

# 파일 업로드 URL
file.upload.url=/uploads
```

### src/main/resources/application-prod.properties

```properties
# 파일 업로드 경로
file.upload.path=/home/ubuntu/uploads

# 파일 업로드 URL
file.upload.url=/uploads
```

### org/choongang/global/config/AppConfig.java

> application.properties 또는 application-prod.properties에 있는 설정 파일을 불러오고 이를 조회할 수 있는 클래스 구성 

```java
package org.choongang.global.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 사이트 설정 로드 및 조회
 *
 */
public class AppConfig {
    private final static ResourceBundle bundle;
    private final static Map<String, String> configs;
    static {
        // 환경 변수 mode에 따라 설정파일을 분리 예) prod이면 application-prod.properties로 읽어온다.
        String mode = System.getenv("mode");
        mode = mode == null || mode.isBlank() ? "":"-" + mode;

        bundle = ResourceBundle.getBundle("application" + mode);
        configs = new HashMap<>();
        Iterator<String> iter = bundle.getKeys().asIterator();
        while(iter.hasNext()) {
            String key = iter.next();
            String value = bundle.getString(key);
            configs.put(key, value);
        }
    }

    public static String get(String key) {
        return configs.get(key);
    }
}
```

### 설정 파일 적용해보기 - 파일 업로드 정적 경로 라우팅 추가 

> 파일 업로드 경로는 개발 환경에 따라 배포 서버에 따라 달라질 수 있으므로 application.properties와 application-prod.properties에 달리 설정 될 수 있다. 
> 정적 경로 라우팅은 webapp/static이 가장 먼저 체크 되고 그 후에는 설정된 파일 업로드 경로다 체크 됩니다.

#### org/choongang/global/router/StaticResourceMappingImpl.java

```java

...

@Service
public class StaticResourceMappingImpl implements StaticResourceMapping {
  ...
  
    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // webapp/static 경로 및 파일 업로드 경로 조회
        File file = getStaticPath(request);
        if (file.exists()) {
            Path source = file.toPath();
            String contentType = Files.probeContentType(source);
            response.setContentType(contentType);

            OutputStream out = response.getOutputStream();

            InputStream in = new BufferedInputStream(new FileInputStream(file));
            out.write(in.readAllBytes());
        }
    }

    // webapp/static 경로 또는 파일 객체 경로 조회 File 객체 조회
    private File getStaticPath(HttpServletRequest request) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        String path = request.getServletContext().getRealPath("/static");
        File file = new File(path + uri);

        // webapp/static 경로에 파일이 없다면 파일 업로드 경로 File 객체 조회
        if (!file.exists()) {
            String uploadPath = AppConfig.get("file.upload.path");
            String uploadUrl = AppConfig.get("file.upload.url");
            if (uploadPath != null && !uploadPath.isBlank() && uploadUrl != null && !uploadUrl.isBlank()) {
                uri = uri.replace(uploadUrl, "");
                file = new File(uploadPath + uri);
            } // endif
        }
        return file;
    }
}
```

> file.upload.path를 D:/uploads라고 하였다면 D:/uploads 디렉토리를 만들고 test.txt 파일을 하나 생성해 봅시다.
> file.upload.url을 /uploads 라고 설정하였다면 브라우저 주소창에 /컨택스트 경로/uploads/test.txt로 정상 접속되는지 content-type은 text/plain으로 정상 응답 되는지 확인 합니다.

# [마이바티스(mybatis) 설정](https://github.com/yonggyo1125/project501_13_jsp/tree/mybatis)

> 마이바티스 매퍼 자원 경로와 파일명이 인터페이스의 패키지 경로 및 파일과 일치하여야 마이바티스가 구현체를 만들어 주게 됩니다. 따라서 src/main/resources에 생성하는 매퍼 경로는 src/main/java에 생성되는 경로와 동일하게 맞춰야 합니다.

## 설정 XML 추가하기 

- src/main/resources/org/choongang/global/config 디렉토리 생성 
- 생성된 경로에 mybatis-config.xml 파일 생성
- 설정은 [mybatis 공식 사이트 - 시작하기](https://mybatis.org/mybatis-3/ko/getting-started.html)를 참고할 것


### src/main/resources/org/choongang/global/config/mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties>
        <property name="driver" value="oracle.jdbc.driver.OracleDriver" />
        <property name="url" value="jdbc:oracle:thin:@localhost:1521:XE" />
        <property name="username" value="PROJECT3" />
        <property name="password" value="oracle" />
        <property name="prodUrl" value="jdbc:oracle:thin:@localhost:1521:XE" />
        <property name="prodUsername" value="PROJECT3" />
        <property name="prodPassword" value="oracle" />
    </properties>
    <environments default="dev">
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
        <environment id="prod">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${prodUrl}"/>
                <property name="username" value="${prodUsername}"/>
                <property name="password" value="${prodPassword}"/>
            </dataSource>
        </environment>
    </environments>

</configuration>
```
- 설정 중 하기 내용은 실제 배포 서버의 데이터베이스 설정에 맞게 지정합니다.

```xml
...
<property name="prodUrl" value="jdbc:oracle:thin:@localhost:1521:XE" />
<property name="prodUsername" value="PROJECT3" />
<property name="prodPassword" value="oracle" />
...
```


## 설정 파일 적용하기 

> 설정 파일(mybatis-config.xml)의 정보를 가지고 마이바티스는 SqlSessionFactory 객체를 생성합니다. SqlSessionFactory 객체는 실제 SQL을 실행할 수 있는 SqlSession 객체를 생성할 수 있는데, SqlSession 객체는 실제로 자주 사용될 수 있습니다. 
> 
> 쉽게 접근할 수 있도록 정적으로 SqlSessionFactory 객체를 생성하고 SqlSession을 생성할 수 있는 정적 메서드를 다음과 가이 추가하여 구성합니다.

### org/choongang/global/config/DBConn.java

```java
package org.choongang.global.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DBConn {
    private static SqlSessionFactory factory;

    static {
        try {
            String mode = System.getenv("mode");
            mode = mode == null || !mode.equals("prod") ? "dev":"prod";

            Reader reader = Resources.getResourceAsReader("org/choongang/global/config/mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(reader, mode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }

    /**
     * 기본 getSession() 메서드를 통해서 SqlSession 객체를 생성하는 경우는
     * 하나의 SQL 쿼리 실행마다 COMMIT을 하도록 autoCommit을 true로 설정합니다.
     * 
     * @return
     */
    public static SqlSession getSession() {
        return getSession(true);
    }
}
```

### 데이터베이스 연결 테스트


#### src/test/java/org/choongang/global/config/DBConnTest.java

```java
package org.choongang.global.config;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 마이바티스 DB 연결 테스트
 * 
 */
public class DBConnTest {
    
    @Test
    @DisplayName("마이바티스 설정에 따라 SqlSession 객체가 정상 생성되는지 테스트")
    void dbConnectionTest() {
        assertDoesNotThrow(() -> {
            SqlSession session = DBConn.getSession();
            System.out.println(session);
        });
    }
}
```

> 테스트 실행 시 예외 발생이 없고 다음과 같이 객체가 정상 생성되어 있어야 합니다. 테스트에 실패한다면 대부분 설정 파일(mybatis-config.xml)에 설정 정보가 정확하지 않은 경우이므로 설정파일을 꼼곰하게 검토해볼 것 

```
org.apache.ibatis.session.defaults.DefaultSqlSession@6692b6c6
```

## 로거 설정 추가

> 마이바티스는 로거를 따로 설정하지 않으면 SQL 실행 내용이나 SQL 파라미터로 바인딩된 데이터를 확인할 수 없습니다. 소스 작성시에 문제가 발생하면 원인을 찾기 어려우므로 SQL 실행 로그가 자세하게 나올 수 있도록 설정합니다.
> 
> 로거는 종류가 다양하나 프로젝트에서는 slf4j-api와 그 구현체인 logback-classic을 사용합니다. 의존성은 추기 구성 때 추가 하였으므로 따로 추가하지는 않지만 누락되었다면 초기 의존성 설정을 확인해 보고 build.gradle 파일에 추가합니다.

- [로거 설정 파일 참고](https://mybatis.org/mybatis-3/ko/logging.html#%EB%91%90%EB%B2%88%EC%A7%B8-%EB%8B%A8%EA%B3%84-logback-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)
 
### src/main/resources/logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration>
<configuration>

    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d [%5p] [%t] - %m%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="stdout"/>
    </root>
  
   <logger name="org.choongang.member.mapper" level="DEBUG" />
</configuration>
```

- root level="INFO" 설정은 전역 기본 로그 레벨이며 info레벨 부터 시작하여 INFO,WARN,ERROR, FATAL 로그를 함게 출력 합니다. 
- mybatis mapper가 추가 되면 해당 mapper 패키지 경로를 debug 레벨로 설정을 추가하여 자세한 로그가 나올 수 있도록 설정을 추가합니다.

```xml

...
<logger name="org.choongang.member.mapper" level="DEBUG" />
...

```


## 회원 기능 개발을 위한 MemberMapper.xml 과 MemberMapper 인터페이스 설정 

- MEMBER 테이블 및 SEQ_MEMBER 시퀀스 생성 

```sql
CREATE TABLE MEMBER (
	USER_NO NUMBER(10) PRIMARY KEY,
	EMAIL VARCHAR2(60) NOT NULL UNIQUE,
	PASSWORD VARCHAR2(65) NOT NULL,
	USER_NAME VARCHAR2(30) NOT NULL,
	USER_TYPE VARCHAR2(10) DEFAULT 'USER' CHECK(USER_TYPE IN ('USER', 'ADMIN')),
	REG_DT DATE DEFAULT SYSDATE,
	MOD_DT DATE
);

CREATE SEQUENCE SEQ_MEMBER;
```

### src/main/resources/org/choongang/member/mapper/MemberMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.choongang.member.mapper.MemberMapper">

</mapper>
```

### src/main/java/org/choongang/member/mapper/MemberMapper.java

```java
package org.choongang.member.mapper;

public interface MemberMapper {

}
```

### src/main/resources/org/choongang/global/config/mybatis-config.xml
> MemberMapper.xml 추가   

```xml
...
<configuration>
...

  <mappers>
    <mapper resource="org/choongang/member/mapper/MemberMapper.xml"/>
  </mappers>
</configuration>
```

### org/choongang/member/services/JoinService.java

> private final MemberMapper mapper; 의존성 추가

```java

...

@Service
@RequiredArgsConstructor
public class JoinService {
    private final JoinValidator validator;
    private final MemberMapper mapper;
}
```

## 매퍼 의존성 자동 주입 및 서블릿 기본 객체 요청시마다 갱신되도록 처리 

> 마이바티스 매퍼의 패키지 경로를 지정하는 애노테이션 @MapperScan에 매퍼 인터페이스가 정의된 패키지들을 설정하면 자동 스캔하여 생성자 매개변수에 정의된 매퍼 객체를 생성하여 주입해 주게 됩니다.

- @MapperScan : 매퍼 검색 패키지 범위를 설정한다. 
- 예)

```java
@MapperScan({
        "org.choongang.member.mapper",
        "org.choongang.board.mapper"
})
public class MapperProvider {
  ...
}
```
> 위 설정은 org.choongang.member.mapper, org.choongang.board.mapper 두개의 패키지에 정의된 매퍼 인터페이스들이 검색 범위가 된다.

- MapperProvider : 매퍼 인터페이스 정의를 찾아서 생성 및 반환

### org/choongang/global/config/annotations/mybatis/MapperScan.java

```java
package org.choongang.global.config.annotations.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 매퍼 스캔 경로 설정
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperScan {
    String[] value();
}
```

### org/choongang/global/config/containers/mybatis/MapperProvider.java

```java 
package org.choongang.global.config.containers.mybatis;

import org.choongang.global.config.DBConn;
import org.choongang.global.config.annotations.mybatis.MapperScan;

import java.util.Arrays;

@MapperScan({"org.choongang.member.mapper"})
public class MapperProvider {

    public static MapperProvider instance;

    private MapperProvider() {}

    public static MapperProvider getInstance() {
        if (instance == null) {
            instance = new MapperProvider();
        }
        return instance;
    }

    public <T> T getMapper(Class clz) {

        MapperScan mapperScan = getClass().getAnnotation(MapperScan.class);
        boolean isMapper = Arrays.stream(mapperScan.value()).anyMatch(s -> s.startsWith(clz.getPackageName()));

        if (isMapper) {
            return (T)DBConn.getSession().getMapper(clz);
        }

        return null;
    }
}
```


### org/choongang/global/router/DispatcherServlet.java
> DispatcherServlet 요청 처리 메서드 service(...)에 HttpSession도 빈으로 등록
> HttpServletRequest, HttpServletResponse, HttpSession과 같은 객체는 매청마다 다른 값을 가지고 있는 객체로 새로 생성이 되는데, 대부분의 객체가 싱글톤 객체이고 그 의존성이 고정되어 버린다. 이를 방지하기 위해 서블릿 기본 객체는 매요청시마다 갱신한다.

```java

...

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        ...
      
        bc.addBean(HttpSession.class.getName(), request.getSession());

        bc.loadBeans();

        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
    }
}

```


### org/choongang/global/config/containers/BeanContainer.java
> 마이바티스 매퍼와 서블릿 기본 객체를 갱신하는 로직 반영 

```java
...

public class BeanContainer {
    
    ...

    private MapperProvider mapperProvider; // 마이바티스 매퍼 조회

    public BeanContainer() {
        beans = new HashMap<>();
        mapperProvider = MapperProvider.getInstance();
    }

    public void loadBeans() {
        // 패키지 경로 기준으로 스캔 파일 경로 조회
        try {
            
            ...

            for (Class clazz : classNames) {
                // 인터페이스는 동적 객체 생성을 하지 않으므로 건너띄기
                if (clazz.isInterface()) {
                    continue;
                }

                // 애노테이션 중 Controller, RestController, Component, Service 등이 TYPE 애노테이션으로 정의된 경우 beans 컨테이너에 객체 생성하여 보관
                // 키값은 전체 클래스명, 값은 생성된 객체
                String key = clazz.getName();

                /**
                 *  이미 생성된 객체라면 생성된 객체로 활용
                 *  매 요청시마다 새로 만들어야 객체가 있는 경우 갱신 처리
                 *
                 *  매 요청시 새로 갱신해야 하는 객체
                 *      - HttpServletRequest
                 *      - HttpServletResponse
                 *      - HttpSession session
                 *      - Mybatis mapper 구현 객체
                 */

                if (beans.containsKey(key)) {
                    updateObject(beans.get(key));
                    continue;
                }


              ...



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        ...
      
    /**
     * 의존성의 의존성을 재귀적으로 체크하여 필요한 의존성의 객체를 모두 생성한다.
     *
     * @param con
     */
    private List<Object> resolveDependencies(String key, Constructor con) throws Exception {
        List<Object> dependencies = new ArrayList<>();
        if (beans.containsKey(key)) {
            dependencies.add(beans.get(key));
            return dependencies;
        }

        Class[] parameters = con.getParameterTypes();
        if (parameters.length == 0) {
            Object obj = con.newInstance();
            dependencies.add(obj);
        } else {
            for(Class clazz : parameters) {
                /**
                 * 인터페이스라면 마이바티스 매퍼일수 있으므로 매퍼로 조회가 되는지 체크합니다.
                 * 매퍼로 생성이 된다면 의존성 주입이 될 수 있도록 dependencies에 추가
                 *
                  */
                if (clazz.isInterface()) {
                    Object mapper = mapperProvider.getMapper(clazz);
                    if (mapper != null) {
                        dependencies.add(mapper);
                        continue;
                    }
                }

                Object obj = beans.get(clazz.getName());
                if (obj == null) {
                    Constructor _con = clazz.getDeclaredConstructors()[0];

                    if (_con.getParameterTypes().length == 0) {
                        obj = _con.newInstance();
                    } else {
                        List<Object> deps = resolveDependencies(clazz.getName(), _con);
                        obj = _con.newInstance(deps.toArray());
                    }
                }
                dependencies.add(obj);
            }
        }


        return dependencies;
    }

      ...

    /**
     * 컨테이너에 이미 담겨 있는 객체에서 매 요청시마다 새로 생성이 필요한 의존성이 있는 경우
     * 갱신 처리
     *  - HttpServletRequest
     *  - HttpServletResponse
     *  - HttpSession session
     *  - Mybatis mapper 구현 객체
     *
     * @param bean
     */
    private void updateObject(Object bean) {
        // 인터페이스인 경우 갱신 배제
        if (bean.getClass().isInterface()) {
            return;
        }

        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class clz = field.getType();
            try {

                /**
                 * 필드가 마이바티스 매퍼 또는 서블릿 기본 객체(HttpServletRequest, HttpServletResponse, HttpSession) 이라면 갱신
                 *
                 */
                
                Object mapper = mapperProvider.getMapper(clz);

                // 그외 서블릿 기본 객체(HttpServletRequest, HttpServletResponse, HttpSession)이라면 갱신
                if (clz == HttpServletRequest.class || clz == HttpServletResponse.class || clz == HttpSession.class || mapper != null) {
                    field.setAccessible(true);
                }

                if (clz == HttpServletRequest.class) {
                    field.set(bean, getBean(HttpServletRequest.class));
                } else if (clz == HttpServletResponse.class) {
                    field.set(bean, getBean(HttpServletResponse.class));
                } else if (clz == HttpSession.class) {
                    field.set(bean, getBean(HttpSession.class));
                } else if (mapper != null) { // 마이바티스 매퍼
                    field.set(bean, mapper);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
```

# [에러페이지 구성](https://github.com/yonggyo1125/project501_13_jsp/tree/exception)

> 404나 서블릿 기본 오류 500은 /WEB-INF/templates/errors/404.jsp, /WEB-INF/templates/errors/500.jsp을 통해서 출력될 수 있도록 처리
> 응답 코드, 요청 URL, 요청 방식과 같은 기본 메세지를 EL 속성으로 추가(status, requestUrl, method) 
> 기타 사용자 정의 예외에 의해 유입되는 경우 해당 예외가 정의된 뷰로 보일수 있도록 @ExceptionHandler에 발생할 수 있는 예외를 정의하고 
> 이를 라우터에서 ExceptionHandlerService를 통해서 처리합니다.

## 에러 페이지 및 응답 처리 

### org/choongang/global/exceptions/ExceptionHandler.java

```java
package org.choongang.global.exceptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {
    Class<? extends  Throwable>[] value();
}
```

### org/choongang/global/exceptions/ExceptionHandlerService.java

```java
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
```

### org/choongang/global/router/RouterService.java

```java

...

@Service
@RequiredArgsConstructor
public class RouterService {

  private final HandlerMappingImpl handlerMapping;
  private final HandlerAdapterImpl handlerAdapter;
  private final StaticResourceMappingImpl staticResourceMapping;
  private final ExceptionHandlerService exceptionHandlerService;

  /**
   * 컨트롤러 라우팅
   *
   */
  public void route(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    List<Object> data = null;
    try {
      data = handlerMapping.search(req);

      // 요청 주소
      String requestUrl = req.getRequestURI();
      requestUrl = req.getQueryString() == null || req.getQueryString().isBlank() ? requestUrl : requestUrl + "?" + req.getQueryString();
      req.setAttribute("requestUrl", requestUrl);

      // 요청 메서드
      req.setAttribute("method", req.getMethod());

      if (data == null) {

        /**
         *  처리 가능한 컨트롤러를 못찾은 경우 지정된 정적 경로에 파일이 있는지 체크 하고
         *  해당 자원을 파일로 읽어 온 후 파일에 맞는 Content-Type으로 응답 헤더 추가 및 Body쪽에 출력하여 보일 수 있도록 한다.
         *  정적 경로에도 파일을 찾을 수 없다면 404 응답 코드를 내보낸다.
         */

        // 정적 자원이라면 정적 라우팅 처리
        if (staticResourceMapping.check(req)) {
          staticResourceMapping.route(req, res);
          return;
        }

        // 컨트롤러도 발견하지 못하고 정적 라우팅도 아니라면 404
        int status = HttpServletResponse.SC_NOT_FOUND;
        res.setStatus(status);

        // 없는 페이지 응답 상태 코드 추가
        req.setAttribute("status", status);

        // 404 없는 페이지 출력
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/templates/errors/404.jsp");
        rd.forward(req, res);
        return;
      }

      // 찾은 컨트롤러 요청 메서드를 실행
      handlerAdapter.execute(req, res, data);
    } catch (Exception e) {
      req.setAttribute("message", e.getMessage());
      req.setAttribute("exception", e);
      if (e instanceof ServletException || e instanceof IOException) {
        int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        res.setStatus(status);
        req.setAttribute("status", status);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/templates/errors/500.jsp");
        rd.forward(req, res);

        e.printStackTrace();
        return;
      }

      // 예외 페이지 처리
      if (data != null) {
        exceptionHandlerService.handle(e, data.get(0));
      }
    }

  }
}
```

### 에러 출력 페이지 템플릿 추가

#### webapp/WEB-INF/templates/errors/404.jsp 

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main>
    <h1>${status} 없는 페이지 입니다.</h1>
    <h2>${method} ${requestUrl}</h2>

</layout:main>
```

### webapp/WEB-INF/templates/errors/500.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main>
${message}
</layout:main>
```

### webapp/WEB-INF/templates/errors/error.jsp

```jsp 
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main>
${message}
</layout:main>
```

### ExceptionHandler 적용 예

> @ExceptionHandler를 컨트롤러에 지정하면 가장 먼저 우선 순위를 가집니다. 다만 없는 경우 해당 패키지 적용 범위에 있는 ControllerAdvice에 정의된 @ExceptionHandler를 찾아 적용합니다.
> @ExceptionHandler로 등록된 메서드에는 발생한 예외 객체, 서블릿 기본 객체인 HttpServletRequest, HttpServletResponse, HttpSession 객체를 필요에 따라서 주입할 수 있습니다.
> 컨트롤러가 @RestController이거나 @RestControllerAdvice로 정의된 경우 응답은 JSON으로 출력이 되고 그 반대의 경우는 반환값으로 템플릿을 찾아서 오류 페이지를 출력합니다.

#### 컨트롤러 적용 예

```java

...

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    ...
  
    @ExceptionHandler(RuntimeException.class)
    public String errorHandler(RuntimeException e1, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        
      ...
      
      return "errors/error";
    } 
}

```


#### ControllerAdvice 적용 예 

```java
...

@ControllerAdvice("org.choongang")
public class CommonControllerAdvice {

  ...

    /**
     * 공통 에러 페이지 처리
     * 
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletRequest request) {

        return "errors/error";
    }
}
```


## 컨트롤러 실행 전 공통 처리 정의 - Advice 

> 공통 속성에 대한 처리를 합니다. 특정 패키지 범위에 정의한 컨트롤러 전역에 유지할 속성과 ExceptionHandler 적용 처리를 합니다. 
> @ControllerAdvice, @RestControllerAdvice가 붙어 있는 클래스는 공통 속성에 대한 공유와 에러 공통 처리를 담당하게 됩니다. 
> 이 역시 자동 스캔 범위에 해당하므로 BeanContainer 자동 스캔 범위로 추가하며 HandlerAdapterImpl에 컨트롤러 메서드 실행 전 @ModelAttribute로 지정된 메서드를 호출하고 공통 속성으로 처리합니다.
> @ModelAttribute는 직접 설정한 이름 또는 메서드명으로 request 범위의 속성명으로 지정이 되며, 반환값이 값으로 등록됩니다.


### org/choongang/global/config/annotations/ModelAttribute.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelAttribute {
    String value() default "";
}
```


### org/choongang/global/config/annotations/ControllerAdvice.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerAdvice {
    String[] value();
}
```

### org/choongang/global/config/annotations/RestControllerAdvice.java

```java
package org.choongang.global.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestControllerAdvice {
    String[] value();
}
```



### org/choongang/global/advices/CommonControllerAdvice.java

```java 
package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.global.exceptions.ExceptionHandler;

@ControllerAdvice("org.choongang")
public class CommonControllerAdvice {

    @ModelAttribute("commonValue2")
    public String commonValue() {
        return "공통 값 속성 추가 테스트";
    }

    /**
     * 공통 에러 페이지 처리
     * 
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletRequest request) {

        return "errors/error";
    }
}
```

### org/choongang/global/advices/RestCommonControllerAdvice.java

```java
package org.choongang.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.RestControllerAdvice;
import org.choongang.global.exceptions.ExceptionHandler;

@RestControllerAdvice("org.choongang")
public class CommonRestControllerAdvice {

    /**
     * Rest 공통 에러 출력 처리
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, HttpServletRequest request) {

        return "error";
    }
}

```


### org/choongang/global/config/containers/BeanContainer.java

> @ControllerAdvice와 @RestControllerAdvice를 자동 스캔 범위에 추가

```java

...

public class BeanContainer {
   
  ...

    public void loadBeans() {
        // 패키지 경로 기준으로 스캔 파일 경로 조회
        try {
            String rootPath = new File(getClass().getResource("../../../").getPath()).getCanonicalPath();
            String packageName = getClass().getPackageName().replace(".global.config.containers", "");
            List<Class> classNames = getClassNames(rootPath, packageName);

            for (Class clazz : classNames) {
                
                ...

                boolean isBean = false;
                for (Annotation anno : annotations) {
                    if (anno instanceof Controller || anno instanceof RestController || anno instanceof Service || anno instanceof Component || anno instanceof ControllerAdvice || anno instanceof RestControllerAdvice)  {
                        isBean = true;
                        break;
                    }
                }
               
              
              ...

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ...
}

```


### org/choongang/global/router/HandlerAdapterImpl.java

```java

...

@Service
public class HandlerAdapterImpl implements HandlerAdapter {

    private final ObjectMapper om;
    private final HandlerControllerAdvice handlerControllerAdvice;

    public HandlerAdapterImpl() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        handlerControllerAdvice = BeanContainer.getInstance().getBean(HandlerControllerAdvice.class);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data) {

      ...

        /* 요청 메서드 호출 S */
        try {
            // controller 적용 범위  Advice 처리
            handlerControllerAdvice.handle(controller);

            Object result = method.invoke(controller, args.toArray());

          ...

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        /* 요청 메서드 호출 E */
    }
    
    ...
  
}
```


