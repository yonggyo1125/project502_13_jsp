package org.choongang.pokemon.game.controllers;

import lombok.Data;

@Data
public class GameLogSearch {
    private int page;
    private int limit;
    private int offset;
    private int endRows;
    private String skey;
}
