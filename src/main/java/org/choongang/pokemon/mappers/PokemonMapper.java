package org.choongang.pokemon.mappers;

import org.choongang.pokemon.entities.PokemonDetail;

public interface PokemonMapper {
    int register(PokemonDetail params);
    PokemonDetail get(long seq);
}
