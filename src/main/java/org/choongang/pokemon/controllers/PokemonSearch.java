package org.choongang.pokemon.controllers;

import lombok.Data;

@Data
public class PokemonSearch {
    private int page = 1;
    private int limit = 20;
}
