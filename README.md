# 목차
1. [사이트 기본 구성](https://github.com/yonggyo1125/project501_13_jsp?tab=readme-ov-file#%EC%B4%88%EA%B8%B0-%EC%84%A4%EC%A0%95)
2. [게시판 만들기](https://github.com/yonggyo1125/project501_13_jsp/tree/board)
3. [포켓몬 도감 가이드](https://github.com/yonggyo1125/project501_13_jsp/tree/pokemon)

--- 

# 게시판 만들기 

## 사이트 기본 UI 구성 

## 회원 인증, 인가 기능 구성 

## 관리자 구성하기 

### 게시판 설정 테이블 

```sql
CREATE TABLE BOARD (
	BID VARCHAR2(30) PRIMARY KEY,
	BNAME VARCHAR2(60) NOT NULL,
	ROWS_PER_PAGE NUMBER(4) DEFAULT 20,
	ACTIVE NUMBER(1) DEFAULT 0,
	ACTIVE_CATEGORY NUMBER(1) DEFAULT 0,
	CATEGORY CLOB,
	AUTHORITY VARCHAR2(10) DEFAULT 'ALL' CHECK(AUTHORITY IN ('ALL', 'USER', 'ADMIN'))
);
```

```sql
CREATE TABLE BOARD_DATA (
	SEQ NUMBER(10) PRIMARY KEY,
	BID VARCHAR2(30),
	GID VARCHAR2(45),
	POSTER VARCHAR2(40) NOT NULL,
	MEMBER_SEQ NUMBER(10) DEFAULT 0,
	GUEST_PASSWORD VARCHAR2(65),
	CATEGORY VARCHAR2(40),
	NOTICE NUMBER(1) DEFAULT 0,
	SUBJECT VARCHAR2(255) NOT NULL,
	CONTENT CLOB NOT NULL,
	UA VARCHAR2(150), 
	IP VARCHAR2(30),
	REG_DT DATE DEFAULT SYSDATE,
	MOD_DT DATE
);

CREATE SEQUENCE SEQ_BOARD_DATA;
```

## 파일 업로드 구현

```sql
CREATE TABLE FILE_INFO (
	SEQ NUMBER(11) PRIMARY KEY,
	GID VARCHAR2(45) NOT NULL,
	LOCATION VARCHAR2(45),
	FILE_NAME VARCHAR2(100) NOT NULL,
	EXTENSION VARCHAR2(30),
	CONTENT_TYPE VARCHAR2(65),
	DONE NUMBER(1) DEFAULT 0,
	REG_DT DATE DEFAULT SYSDATE
);

CREATE SEQUENCE SEQ_FILE_INFO;
```

## 게시글 쓰기 및 수정 구현

## 게시글 보기, 목록 구현

## 댓글 구현 

---

# 포켓몬 도감

## 참고 사이트

- [https://www.pokemonkorea.co.kr/pokedex](https://www.pokemonkorea.co.kr/pokedex)

## API Rest 요청 범용 기능 추가

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
	TYPE1 VARCHAR2(30),
	TYPE2 VARCHAR2(30),
	RAW_DATA CLOB
);
```


# 포켓몬 기초 데이터 조회 및 DB 반영

> 현재 포켓몬 총 갯수는 1302개 이므로 DB에 전체 데이터 저장해도 무리가 없습니다.
> 일괄 반영처리 하지만 총 1303번 API 요청이 진행되므로 데몬 쓰레드 방식으로 동작하도록 구성한다.



# 페이징 구현

### org/choongang/global/Pagination.java

```java
package org.choongang.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Pagination {

    private int page;
    private int total;
    private int ranges;
    private int limit;

    private int firstRangePage; // 구간별 첫 페이지
    private int lastRangePage; // 구간별 마지막 페이지

    private int prevRangePage; // 이전 구간 첫 페이지 번호
    private int nextRangePage; // 다음 구간 첫 페이지 번호

    private int totalPages; // 전체 페이지 갯수
    private String baseURL; // 페이징 쿼리스트링 기본 URL

    /**
     *
     * @param page : 현재 페이지
     * @param total : 전체 레코드 갯수
     * @param ranges : 페이지 구간 갯수
     * @param limit : 1페이지 당 레코드 갯수
     */
    public Pagination(int page, int total, int ranges, int limit, HttpServletRequest request) {

        page = Math.max(page, 1);
        total = Math.max(total, 1);
        ranges = Math.max(ranges, 1);
        limit = Math.max(limit, 1);

        // 전체 페이지 갯수
        int totalPages = (int)Math.ceil(total / (double)limit);


        // 구간 번호
        int rangeCnt = (page - 1) / ranges;
        int firstRangePage = rangeCnt * ranges + 1;
        int lastRangePage = firstRangePage + ranges - 1;
        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage;

        // 이전 구간 첫 페이지
        if (rangeCnt > 0) {
            prevRangePage = firstRangePage - ranges;
        }

        // 다음 구간 첫 페이지
        // 마지막 구간 번호
        int lastRangeCnt = (totalPages - 1) / ranges;
        if (rangeCnt < lastRangeCnt) { // 마지막 구간이 아닌 경우 다음 구간 첫 페이지 계산
            nextRangePage = firstRangePage + ranges;
        }

        /*
         * 쿼리스트링 값 유지 처리 - 쿼리스트링 값 중에서 page만 제외하고 다시 조합
         *  예) ?orderStatus=CASH&name=...&page=2 -> ?orderStatus=CASH&name=...
         *      ?page=2 -> ?
         *      없는 경우 -> ?
         *
         *      &로 문자열 분리
         *      { "orderStatus=CASH", "name=....", "page=2" }
         */

        String baseURL = "?";
        if (request != null) {
            String queryString = request.getQueryString();
            if (queryString != null && !queryString.isBlank()) {
                queryString = queryString.replace("?", "");

                baseURL += Arrays.stream(queryString.split("&"))
                        .filter(s -> !s.contains("page="))
                        .collect(Collectors.joining("&"));

                baseURL = baseURL.length() > 1 ? baseURL += "&" : baseURL;
            }
        }

        this.page = page;
        this.total = total;
        this.ranges = ranges;
        this.limit = limit;
        this.firstRangePage = firstRangePage;
        this.lastRangePage = lastRangePage;
        this.totalPages = totalPages;
        this.baseURL = baseURL;
    }

    public Pagination(int page, int total, int ranges, int limit) {
        this(page, total, ranges, limit, null);
    }

    public List<String[]> getPages() {
        // 0 : 페이지 번호, 1 : 페이지 URL - ?page=페이지번호


        return IntStream.rangeClosed(firstRangePage, lastRangePage)
                .mapToObj(p -> new String[] { String.valueOf(p),
                        baseURL + "page=" + p})
                .toList();

    }
}
```

## 포켓몬 검색 총 갯수 조회 추가

#### resources/org/choongang/pokemon/mappers/PokemonMapper.xml

```xml

...
<select id="getTotal" resultType="int">
    SELECT COUNT(*) FROM POKEMON
    <where>
        <if test="skey != null and !skey.equals('')">
            <bind name="keyword" value="'%' + _parameter.getSkey() + '%'" />
            AND NAME || NAME_KR || DESCRIPTION LIKE #{keyword}
        </if>
    </where>
</select>
...

```

### org/choongang/pokemon/mappers/PokemonMapper.java

```java
...

public interface PokemonMapper {
    
    ...

    // 포켓몬 목록 총 갯수
    int getTotal(PokemonSearch search);
}

```

### org/choongang/pokemon/services/PokemonInfoService.java

```java

...

@Service
@RequiredArgsConstructor
public class PokemonInfoService {
    
    ...

    public ListData<PokemonDetail> getList(PokemonSearch search) {

        int page = search.getPage();
        int limit = search.getLimit();
        int offset = (page - 1) * limit + 1; // 레코드 검색 시작 위치
        int endRows = offset + limit; // 레코드 검색 종료 위치

        search.setOffset(offset);
        search.setEndRows(endRows);

        List<PokemonDetail> items = mapper.getList(search);

        /* 페이징 처리 S */
        int total = mapper.getTotal(search);

        Pagination pagination = new Pagination(page, total, 10, limit, BeanContainer.getInstance().getBean(HttpServletRequest.class));
        /* 페이징 처리 E */
        return new ListData<>(items, pagination);
    }

    ...
}
```




### src/main/webapp/WEB-INF/tags/utils/pagination.tag

```jsp
<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${pagination != null}">
    <div class='pagination'>
        <c:if test="${pagination.prevRangePage > 0}">
            <a href="<c:url value='${pagination.baseURL}page=1' />">처음</a>
            <a href="<c:url value='${pagination.baseURL}page=${pagination.prevRangePage}' />">이전</a>
        </c:if>
        <c:forEach var="page" items="${pagination.pages}">
            <a class="page${pagination.page == Integer.parseInt(page[0])?' on':''}" href="<c:url value='${page[1]}' />">${page[0]}</a>
        </c:forEach>
        <c:if test="${pagination.nextRangePage > 0}">
            <a href="<c:url value='${pagination.baseURL}page=${pagination.nextRangePage}' />">다음</a>
            <a href="<c:url value='${pagination.baseURL}page=${pagination.totalPages}' />">마지막</a>
        </c:if>
    </div>
</c:if>
```

### 페이징 적용 예 - src/main/webapp/WEB-INF/templates/pokemon/index.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<c:url var="searchUrl" value="/pokemon" />

<layout:main>
<section class="layout-width">
    ...
</section>
<util:pagination />
</layout:main>

```

--- 
# 마이페이지  - 회원 정보 수정

### org/choongang/mypage/controllers/RequestProfile.java 

```java
package org.choongang.mypage.controllers;

import lombok.Data;

@Data
public class RequestProfile {
    private String userName;
    private String password;
    private String confirmPassword;
}
```

### org/choongang/mypage/validators/ProfileUpdateValidator.java

```java
package org.choongang.mypage.validators;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.exceptions.UnAuthorizedException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.MemberUtil;
import org.choongang.mypage.controllers.RequestProfile;

@Component
@RequiredArgsConstructor
public class ProfileUpdateValidator implements Validator<RequestProfile>, RequiredValidator {

    private final MemberUtil memberUtil;

    @Override
    public void check(RequestProfile form) {

        // 로그인 여부 체크 
        checkTrue(memberUtil.isLogin(), new UnAuthorizedException());

        String userName = form.getUserName();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        int status = HttpServletResponse.SC_BAD_REQUEST;

        checkRequired(userName, new AlertException("회원명을 입력하세요.", status));

        if (password != null && !password.isBlank()) {
            checkRequired(confirmPassword, new AlertException("비밀번호를 확인하세요.", status));

            /* 비밀번호 자리수 체크 */
            checkTrue(password.length() >= 8, new AlertException("비밀번호는 8자리 이상 입력하세요.", status));

            /* 비밀번호 및 비밀번호 확인 일치 여부 */
            checkTrue(password.equals(confirmPassword), new AlertException("비밀번호가 일치하지 않습니다.", status));
        }
    }
}
```

### org/choongang/mypage/services/ProfileService.java

```java

```


---
# 회원 프로필 - 마이포켓몬 등록 기능 가이드

- SQL에 다음 항목 추가

```sql
ALTER TABLE MEMBER ADD MY_POKEMON_SEQ NUMBER(11) DEFAULT 0; 
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


# 마이 포켓몬 

```sql
CREATE TABLE MY_POKEMON (
	USER_NO NUMBER(11),
	SEQ NUMBER(11),
	PRIMARY KEY(USER_NO, SEQ)
);
```