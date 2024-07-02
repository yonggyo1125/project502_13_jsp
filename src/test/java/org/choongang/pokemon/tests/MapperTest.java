package org.choongang.pokemon.tests;

import org.choongang.global.config.DBConn;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.mappers.PokemonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.LongStream;

public class MapperTest {

    private PokemonMapper mapper;

    @BeforeEach
    void init() {
        mapper = DBConn.getSession().getMapper(PokemonMapper.class);
    }

    @Test
    void registerTest() {
        LongStream.rangeClosed(1, 10).forEach(i -> mapper.registerMyPokemon(81, i));
    }

    @Test
    void getMyPokemonsTest() {
        List<PokemonDetail> items = mapper.getMyPokemons(81L);
        items.forEach(System.out::println);
    }

    @Test
    void deleteMyPokemon() {
        int result = mapper.deleteMyPokemon(81L, 1L);
        System.out.println(result);
    }

    @Test
    void deleteAllMyPokemon() {
        int result = mapper.deleteAllMyPokemon(81L);
        System.out.println(result);
    }
}
