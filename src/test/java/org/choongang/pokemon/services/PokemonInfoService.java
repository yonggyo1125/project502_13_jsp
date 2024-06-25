package org.choongang.pokemon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.AppConfig;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.services.ApiRequestService;
import org.choongang.global.services.ObjectMapperService;
import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.api.ApiResult;
import org.choongang.pokemon.entities.api.Item;
import org.choongang.pokemon.entities.api.Pokemon;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 포켓몬 API 목록 조회 및 상세 조회 서비스
 *
 */
@Service
@RequiredArgsConstructor
public class PokemonInfoService {
    private final ApiRequestService service;
    private final ObjectMapperService om;
    private final PokemonSaveService saveService;

    // 포켓몬 API URL
    private String apiUrl = AppConfig.get("pokemon.api.url");

    /**
     * 포켓몬 API V2에서 지원하는 API 요청 목록
     *
     * @return
     */
    public Map<String, String> getApiAll() {

        HttpResponse<String> res = service.request(apiUrl);

        try {
            Map<String, String> data = om.readValue(res.body(), new TypeReference<>() {});

            return data;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 포켓몬 목록 조회
     *
     * @param search
     */
    public List<Item> getApiList(PokemonSearch search) {
        int page = search.getPage() < 1 ? 1 : search.getPage();
        int limit = search.getLimit() < 1 ? 20 : search.getLimit();
        int offset = (page - 1) * limit;


        List<Item> items = null;

        String url = String.format(apiUrl + "/pokemon?offset=%d&limit=%d", offset, limit);
        System.out.println(url);
        HttpResponse<String> response = service.request(url);
        if (response.statusCode() == HttpServletResponse.SC_OK) {

            try {
                ApiResult<Item> apiResult = om.readValue(response.body(), new TypeReference<>() {});

                items = apiResult.getResults().stream().toList();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return items == null ? Collections.EMPTY_LIST : items;
    }

    /**
     * 조회 번호로 포켓몬 상세 정보 조회 업데이트
     *
     * @param seq
     * @return
     */
    public Optional<Pokemon> update(long seq) {

        String url = apiUrl + "/pokemon/" + seq;

        Pokemon pokemon = null;
        HttpResponse<String> response = service.request(url);

        if (response.statusCode() == HttpServletResponse.SC_OK) {
            try {
                pokemon = om.readValue(response.body(), Pokemon.class);
                pokemon.setRawData(response.body());

                saveService.save(pokemon);

            } catch (JsonProcessingException e) {
               e.printStackTrace();
            }
        }


        return Optional.ofNullable(pokemon);
    }

    /**
     * 포켓몬 데이터 일괄 업데이트
     * 현재 총 등록된 포켓몬 목록은 1302개로 전체 일괄 업데이트 해도 문제 없을 듯
     *
     */
    public void updateAll() {
        Thread th = new Thread(() -> {
            PokemonSearch search = new PokemonSearch();
            search.setPage(1);
            search.setLimit(2000);
            List<Item> items = getApiList(search);
            items.forEach(item -> {
                String url = item.getUrl();
                Pattern p = Pattern.compile("/pokemon/(\\d*)/");
                Matcher matcher = p.matcher(url);
                if (matcher.find()) {
                    long seq = Long.parseLong(matcher.group(1));
                    try {
                        update(seq);
                    } catch (Exception e) {
                        // 이미 추가된 포켓몬은 seq 번호 중복으로 무결성 제약조건 발생, 해당 건은 건너 뛴다.
                    }
                }

            });
        });

        th.setDaemon(true);
        th.start();
    }
}
