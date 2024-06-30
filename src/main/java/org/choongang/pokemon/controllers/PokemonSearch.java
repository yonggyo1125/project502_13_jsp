package org.choongang.pokemon.controllers;

import lombok.Data;

@Data
public class PokemonSearch {
    private int page = 1;
    private int limit = 20; // 한페이지당 출력될 레코드 갯수
    private int offset;
    private int endRows;
    private String skey; // 검색 키워드
}
