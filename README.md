# 목차
1. [사이트 기본 구성](https://github.com/yonggyo1125/project501_13_jsp?tab=readme-ov-file#%EC%B4%88%EA%B8%B0-%EC%84%A4%EC%A0%95)
2. [게시판 만들기](https://github.com/yonggyo1125/project501_13_jsp/tree/board)
3. [포켓몬 도감 가이드](https://github.com/yonggyo1125/project501_13_jsp/tree/pokemon)

---

# 참고 사이트 

- [https://www.pokemonkorea.co.kr/pokedex](https://www.pokemonkorea.co.kr/pokedex)

# API Rest 요청 범용 기능 추가 

> java.net.HttpClient 활용하여 범용 기능 구현


### org/choongang/global/services/ApiRequestService.java

```java
package org.choongang.global.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.choongang.global.config.annotations.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Getter
@Service
public class ApiRequestService {

    private final ObjectMapper om;

    public ApiRequestService() {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
    }

    /**
     * REST 요청 처리
     *
     * @param url : 요청 URL
     * @param method : 요청 메서드 - GET, POST, PUT, PATCH, DELETE
     * @param headers : 요청 헤더
     * @param data : POST, PUT, PATCH의 경우 BODY 데이터
     */
    public HttpResponse<String> request(String url, String method, Map<String, String> headers, Map<String, String> data) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));

        method = method.toUpperCase();

        /* 요청 헤더 추가 S */
        if (headers != null && !headers.isEmpty()) {
            headers.entrySet().stream().forEach(h -> builder.setHeader(h.getKey(), h.getValue()));
        }
        /* 요청 헤더 추가 E */

        /* 요청 바디 추가 S - 요청 바디는 POST, PUT, PATCH일때 적용 */
        if (List.of("POST", "PUT", "PATCH").contains(method)) {
            String defaultContentType = "application/x-www-form-urlencoded";
            String contentType = headers == null ? defaultContentType : headers.getOrDefault("content-type", defaultContentType);

            builder.setHeader("content-type", contentType);

            HttpRequest.BodyPublisher bodyPublisher = data == null ? HttpRequest.BodyPublishers.noBody() : getBodyString(data, contentType);

            if (method.equals("PUT")) {
                builder.PUT(bodyPublisher);
            } else {
                builder.POST(bodyPublisher);
            }
        }
        /* 요청 바디 추가 E */


        try {
            HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));
            return response;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HttpResponse<String> request(String url, String method, Map<String, String> data) {
        return request(url, method, null, data);
    }

    public HttpResponse<String> request(String url, String method) {
        return request(url, method, null);
    }

    public HttpResponse<String> request(String url) {
        return request(url, "GET");
    }

    /**
     * POST, PUT, PATCH 요청 데이터 처리
     *
     * @param data
     * @param contentType
     * @return
     */
    private HttpRequest.BodyPublisher getBodyString(Map<String, String> data, String contentType) {
        String res = null;

        // contentType이 application/json인 경우
        if (contentType.toLowerCase().contains("application/json")) {

            try {
                res = om.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else { // contentType이 application/x-www-form-urlencoded 인 경우
            res = data.entrySet().stream().map(d -> d.getKey() + "=" + d.getValue()).toString();
            res = res == null?"": URLEncoder.encode(res, Charset.forName("UTF-8"));
        }

        return HttpRequest.BodyPublishers.ofString(res, Charset.forName("UTF-8"));
    }

    /**
     * 응답 데이터를 Map으로 변환
     * 
     * @param response
     * @return
     */
    public Map<String, Object> toMap(HttpResponse<String> response) {
        return toMap(response.body());
    }

    public Map<String, Object> toMap(String body) {
        try {
            Map<String, Object> data = om.readValue(body, new TypeReference<>() {});
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 응답 데이터를 String으로 변환
     *
     * @param response
     * @return
     */
    public String toString(HttpResponse<String> response) {
        return response.body();
    }
}
```

## 기능 동작 테스트 

### src/test/java/org/choongang/global/services/tests/ApiRequestServiceTest.java

```java
package org.choongang.global.services.tests;

import org.choongang.global.services.ApiRequestService;
import org.choongang.global.config.containers.BeanContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApiRequestServiceTest {
    private BeanContainer bc;
    private ApiRequestService service;
    private String apiUrl;

    @BeforeEach
    void init() {
        bc = BeanContainer.getInstance();
        service = new ApiRequestService();
        bc.addBean(service);

        apiUrl = "https://pokeapi.co/api/v2/ability/1";

    }

    @Test
    @DisplayName("요청 테스트")
    void requestSuccessTest() {
        assertDoesNotThrow(() -> {
            HttpResponse<String> res = service.request(apiUrl);

            // 정상 응답 - 200
            assertEquals(200, res.statusCode());

            // 응답 헤더 조회 테스트
            System.out.println("----- 응답 헤더 조회 S -----");
            res.headers().map().entrySet().forEach(h -> System.out.println(h.getKey() + ", " + h.getValue()));
            System.out.println("----- 응답 헤더 조회 E -----");

            // 응답 바디 조회 테스트
            System.out.println("----- 응답 바디 조회 S -----");
            String body = res.body();
            System.out.println(body);
            assertTrue(body != null && !body.isBlank());
            System.out.println("----- 응답 바디 조회 E -----");
        });
    }
    
    @Test
    @DisplayName("HttpResponse<String> 형태의 응답을 Map으로 정상 변환되는지 테스트")
    void bodyToMapTest() {
        assertDoesNotThrow(() -> {
            HttpResponse<String> res = service.request(apiUrl);

            Map<String, Object> data = service.toMap(res);
            System.out.println(data);
            assertNotNull(data);
        });
    }

    @Test
    @DisplayName("HttpResponse<String> 형태의 응답을 String으로 정상 변환되는지 테스트")
    void bodyToStringTest() {
        assertDoesNotThrow(() -> {
            HttpResponse<String> res = service.request(apiUrl);
            String data = service.toString(res);
            System.out.println(data);
            assertNotNull(data);
        });

    }
}
```

# 포켓몬 상세 정보 저장 SQL

```sql
CREATE TABLE POKEMON (
	SEQ NUMBER(10) PRIMARY KEY,
	NAME VARCHAR2(60) NOT NULL,
	NAME_KR VARCHAR2(60),
	WEIGHT NUMBER(7) DEFAULT 0,
	HEIGHT NUMBER(7) DEFAULT 0,
	BASE_EXPERIENCE NUMBER(10) DEFAULT 0,
	FRONT_IMAGE VARCHAR2(150),
	BACK_IMAGE VARCHAR2(150),
	DESCRIPTION VARCHAR2(1000),
	RAW_DATA CLOB
);
```


# 포켓몬 기초 데이터 조회 및 DB 반영

> 현재 포켓몬 총 갯수는 1302개 이므로 DB에 전체 데이터 저장해도 무리가 없습니다.
> 일괄 반영처리 하지만 총 1303번 API 요청이 진행되므로 데몬 쓰레드 방식으로 동작하도록 구성한다.




# 회원 프로필 - 마이포켓몬 등록 기능 가이드 

- SQL에 다음 항목 추가

```sql
ALTER TABLE MEMBER ADD MY_POKEMON_SEQ NUMBER(11);
```

## org/choongang/member/entities/Member

> private long myPokemonSeq; 추가 

```java
package org.choongang.member.entities;

import lombok.Builder;
import lombok.Data;
import org.choongang.member.constants.UserType;

import java.time.LocalDateTime;

@Data
@Builder
public class Member {
    private long userNo;
    private String email;
    private String password;
    private String userName;
    private UserType userType = UserType.USER;
    private long myPokemonSeq;
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
```
