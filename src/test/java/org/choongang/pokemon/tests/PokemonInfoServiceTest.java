package org.choongang.pokemon.tests;

import org.choongang.global.config.DBConn;
import org.choongang.global.services.ApiRequestService;
import org.choongang.global.services.ObjectMapperService;
import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.entities.api.Item;
import org.choongang.pokemon.entities.api.Pokemon;
import org.choongang.pokemon.mappers.PokemonMapper;
import org.choongang.pokemon.services.PokemonInfoService;
import org.choongang.pokemon.services.PokemonSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("포켓몬 조회 서비스 테스트")
public class PokemonInfoServiceTest {

    private PokemonInfoService service;

    @BeforeEach
    void init() {
        PokemonMapper mapper = DBConn.getSession().getMapper(PokemonMapper.class);
        PokemonSaveService saveService = new PokemonSaveService(mapper);
        service = new PokemonInfoService(new ApiRequestService(), new ObjectMapperService(), saveService, mapper);
    }

    @Test
    @DisplayName("포켓몬 API V2에서 지원하는 API 요청 목록 조회 테스트")
    void getApiListTest() {
        assertDoesNotThrow(() -> {
            Map<String, String> items = service.getApiAll();
            System.out.println(items);
        });
    }

    @Test
    @DisplayName("포켓몬 목록 API 조회 테스트")
    void getListTest() {
        assertDoesNotThrow(() -> {
            PokemonSearch search = new PokemonSearch();
            search.setPage(1);
            search.setLimit(20);
            List<Item> items = service.getApiList(search);
            System.out.println(items);
        });
    }

    @Test
    @DisplayName("포켓몬 개별 조회 및 업데이트 테스트")
    void getTest() {
        assertDoesNotThrow(() -> {
            service.update(1);
        });
    }

    @Test
    @DisplayName("포켓몬 데이터 일괄 업데이트 테스트")
    void updateAllTest() {
        service.updateAll();
    }

    @Test
    @DisplayName("getList 메서드 테스트")
    void getListDbTest() {
        PokemonSearch search = new PokemonSearch();
        search.setPage(1);
        search.setLimit(5);

        service.getList(search);
    }

    @Test
    @DisplayName("get 메서드 테스트")
    void getDbTest() {
        PokemonDetail data = service.get(1L).orElse(null);
        //System.out.println(data);
        Pokemon pokemon = data.getPokemon();


    }

    @Test
    void getRegExTest() {
        ApiRequestService service = new ApiRequestService();
        HttpResponse<String> response = service.request("https://pokeapi.co/api/v2/pokemon-species/1/");
        String text = response.body();
        text = text.split("names")[1];
        text = text.split("\"name\":\"ko\"")[1];
        text = text.split("\"language\"")[0];
        text = text.split("\"name\":")[1];


        Pattern p = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    void getRegExTest2() {
        ApiRequestService service = new ApiRequestService();
        HttpResponse<String> response = service.request("https://pokeapi.co/api/v2/pokemon-species/1/");
        String text = response.body();
        text = text.split("flavor_text_entries")[1];
        text = text.split("\"name\":\"ko\"")[0];
        Pattern p = Pattern.compile("([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+)");
        Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            String key = matcher.group(1);
            text = text.split(key)[1];
            text = text.split("\",\"language\"")[0];
            String description = key + " " + text;
            System.out.println(description);
        }
    }
    
    @Test
    @DisplayName("랜덤 조회 테스트")
    void getRandomTest() {
        PokemonDetail data = service.getRandom().orElse(null);
        System.out.printf("seq=%d, name=%s, name_kr=%s", data.getSeq(), data.getName(), data.getNameKr());
    }
}
