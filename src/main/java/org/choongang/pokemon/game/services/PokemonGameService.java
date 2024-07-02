package org.choongang.pokemon.game.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.exceptions.PokemonNotFoundException;
import org.choongang.pokemon.game.constants.GameResult;
import org.choongang.pokemon.services.PokemonInfoService;

import static org.choongang.pokemon.game.constants.GameResult.*;

@Service
@RequiredArgsConstructor
public class PokemonGameService {
    private final PokemonInfoService infoService;

    /**
     *
     * @param seq
     * @return boolean - true : 사용자 승리
     *                   false : 컴퓨터 승리
     */
    public GameResult play(long seq) {

        // 사용자가 선택한 포켓몬
        PokemonDetail user = infoService.get(seq).orElseThrow(PokemonNotFoundException::new);
        int userPoint = user.getHeight() * user.getWeight() * user.getBaseExperience();

        // 컴퓨터가 선택한 포켓몬
        PokemonDetail computer = infoService.getRandom().orElseThrow(PokemonNotFoundException::new);
        int computerPoint = computer.getHeight() * computer.getWeight() * computer.getBaseExperience();

        GameResult result = DRAW; // 무승부
        if (userPoint > computerPoint) {
            result = WIN; // 사용자 승
        } else if (computerPoint > userPoint) {
            result = LOSE; // 컴퓨터 승
        }

        // 기록

        return result;
    }
}
