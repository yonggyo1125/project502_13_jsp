package org.choongang.pokemon.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.config.AppConfig;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.global.services.ApiRequestService;
import org.choongang.global.services.ObjectMapperService;
import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.entities.api.ApiResult;
import org.choongang.pokemon.entities.api.Item;
import org.choongang.pokemon.entities.api.Pokemon;
import org.choongang.pokemon.mappers.PokemonMapper;

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
    private final PokemonMapper mapper;

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

                /* 포켓몬 한글 이름, 한글 설명 추출 S */
                HttpResponse<String> res = service.request("https://pokeapi.co/api/v2/pokemon-species/" + seq);
                String body = res.body();

                // 이름 추출 S
                String text = body;
                text = text.split("names")[1];
                text = text.split("\"name\":\"ko\"")[1];
                text = text.split("\"language\"")[0];
                text = text.split("\"name\":")[1];


                Pattern p = Pattern.compile("\"([^\"]+)\"");
                Matcher matcher = p.matcher(text);
                if (matcher.find()) {
                    pokemon.setNameKr(matcher.group(1));
                }
                // 이름 추출 E

                // 설명 추출 S
                text = body;
                text = text.split("flavor_text_entries")[1];
                text = text.split("\"name\":\"ko\"")[0];
                Pattern p2 = Pattern.compile("([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+)");
                Matcher matcher2 = p2.matcher(text);
                if (matcher2.find()) {
                    String key = matcher2.group(1);
                    text = text.split(key)[1];
                    text = text.split("\",\"language\"")[0];
                    String description = key + " " + text;
                    pokemon.setDescription(description);
                }
                // 설명 추출 E

                /* 포켓몬 한글 이름, 한글 설명 추출 E */


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
        //Thread th = new Thread(() -> {
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
                        e.printStackTrace();
                        // 이미 추가된 포켓몬은 seq 번호 중복으로 무결성 제약조건 발생, 해당 건은 건너 뛴다.
                    }
                }

            });
        //});

        //th.setDaemon(true);
        //th.start();
    }

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

    public Optional<PokemonDetail> get(long seq) {
        PokemonDetail data = mapper.get(seq);
        convertRawData(data);

        return Optional.ofNullable(data);
    }

    public void convertRawData(PokemonDetail data) {
        if (data != null) {
            String rawData = data.getRawData();
            try {
                Pokemon pokemon = om.readValue(rawData, Pokemon.class);
                data.setPokemon(pokemon); // 원 데이터 변환
            } catch (JsonProcessingException e) {}
        }
    }

    /**
     * 랜덤하게 포켓몬 조회 하기
     *
     * @return
     */
    public Optional<PokemonDetail> getRandom() {
        PokemonDetail data = mapper.getRandom();
        convertRawData(data);

        return Optional.ofNullable(data);
    }
}
