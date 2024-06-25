package org.choongang.pokemon.tests;

import org.choongang.global.services.ApiRequestService;
import org.choongang.global.services.ObjectMapperService;
import org.choongang.pokemon.services.PokemonInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("포켓몬 조회 서비스 테스트")
public class PokemonInfoServiceTest {

    private PokemonInfoService service;

    @BeforeEach
    void init() {
       service = new PokemonInfoService(new ApiRequestService(), new ObjectMapperService());
    }

    @Test
    @DisplayName("포켓몬 API V2에서 지원하는 API 요청 목록 조회 테스트")
    void getApiListTest() {
        assertDoesNotThrow(() -> {
            Map<String, String> items = service.getApiList();
            System.out.println(items);
        });
    }
}
