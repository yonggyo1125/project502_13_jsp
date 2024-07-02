package org.choongang.pokemon.tests;

import org.choongang.global.config.DBConn;
import org.choongang.pokemon.mappers.PokemonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
