package org.choongang.pokemon.mappers;

import org.choongang.pokemon.controllers.PokemonSearch;
import org.choongang.pokemon.entities.PokemonDetail;

import java.util.List;

public interface PokemonMapper {
    int register(PokemonDetail params);
    PokemonDetail get(long seq);
    List<PokemonDetail> getList(PokemonSearch search);
}
