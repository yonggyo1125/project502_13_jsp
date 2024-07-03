package org.choongang.pokemon.mappers;

import org.apache.ibatis.annotations.Param;
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

    List<PokemonDetail> getMyPokemons(long userNo);
    int registerMyPokemon(@Param("userNo") long userNo, @Param("seq") long seq);
    int deleteMyPokemon(@Param("userNo") long userNo, @Param("seq") long seq);
    int deleteAllMyPokemon(long userNo);
    List<Long> getMyPokemonSeqs(long userNo);
    int myPokemonExists(@Param("userNo") long userNo, @Param("seq") long seq);
}
