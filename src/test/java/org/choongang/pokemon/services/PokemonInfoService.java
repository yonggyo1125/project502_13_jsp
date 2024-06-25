package org.choongang.pokemon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.config.AppConfig;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.services.ApiRequestService;
import org.choongang.global.services.ObjectMapperService;
import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.ApiResult;
import org.choongang.pokemon.entities.Pokemon;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

/**
 * 포켓몬 API 목록 조회 및 상세 조회 서비스
 *
 */
@Service
@RequiredArgsConstructor
public class PokemonInfoService {
    private final ApiRequestService service;
    private final ObjectMapperService om;

    // 포켓몬 API URL
    private String apiUrl = AppConfig.get("pokemon.api.url");

    /**
     * 포켓몬 API V2에서 지원하는 API 요청 목록
     *
     * @return
     */
    public Map<String, String> getApiList() {

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
    public Optional<ListData<Pokemon>> getList(PokemonSearch search) {
        int page = search.getPage() < 1 ? 1 : search.getPage();
        int limit = search.getLimit() < 1 ? 20 : search.getLimit();
        int offset = (page - 1) * limit;

        ListData<Pokemon> listData = new ListData<>();

        String url = String.format(apiUrl + "/pokemon?offset=%d&limit=%d", offset, limit);

        HttpResponse<String> response = service.request(url);
        if (response.statusCode() == HttpServletResponse.SC_OK) {

            try {
                ApiResult<Pokemon> apiResult = om.readValue(response.body(), new TypeReference<>() {});
                listData.setTotal(apiResult.getCount());
                listData.setItems(apiResult.getResults());
                System.out.println(listData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return Optional.ofNullable(listData);
    }
}