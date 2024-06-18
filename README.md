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
- 사이트 전체 설정 경로 : src/main/java/choongang/global/config 
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
    String[] value();
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
    String[] value();
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
    String[] value();
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
    String[] value();
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
    String[] value();
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
    String[] value();
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


> org/choongang/global/router/DispatcherServlet.java

- 모든 요청은 여기로 유입되고 요청 패턴에 맞는 주소와 요청 방식을 확인해 보고 적절한 컨트롤러를 실행해 줍니다. 

```java

```