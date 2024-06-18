# 초기 설정 

## 의존성 추가 
- servlet-api, servlet.jsp-api, jstl, lombok, ojdbc11, mybatis, jbcrypt, mockito

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

### 웹 요청 설정 애노테이션 

> src/main/java/choongang/global/config/web/Controller.java
- 이 애노테이션이 붙어 있는 클래스는 컨트롤러로 등록이 됩니다.

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

> org/choongang/global/router/DispatcherServlet.java

- 모든 요청은 여기로 유입되고 요청 패턴에 맞는 주소와 요청 방식을 확인해 보고 적절한 컨트롤러를 실행해 줍니다. 

```java

```