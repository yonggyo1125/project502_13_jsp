package org.choongang.pokemon.mappers;

import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.PokemonDetail;

import java.util.List;

public interface PokemonMapper {
    int register(PokemonDetail params);
    PokemonDetail get(long seq);
    List<PokemonDetail> getList(PokemonSearch search);

    // 포켓몬 랜덤 조회
    PokemonDetail getRandom();

    // 포켓몬 목록 총 갯수
    int getTotal(PokemonSearch search);
}
