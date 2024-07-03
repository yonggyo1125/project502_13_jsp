package org.choongang.pokemon.mappers;

import org.choongang.pokemon.game.controllers.GameLogSearch;
import org.choongang.pokemon.game.entities.GameLog;

import java.util.List;

public interface GameLogMapper {
    List<GameLog> getList(GameLogSearch search);
    int getTotal(GameLogSearch search);
    int register(GameLog log);
}
