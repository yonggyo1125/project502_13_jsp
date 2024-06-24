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
