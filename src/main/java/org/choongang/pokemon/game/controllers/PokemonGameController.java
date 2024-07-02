package org.choongang.pokemon.game.controllers;

import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequestMapping("/pokemon/game")
public class PokemonGameController {

    /**
     * 1단계 : 카드 선택
     * 2단계 : 게임 시작, 결과
     *
     * @return
     */
    @GetMapping("/step1")
    public String step1() {

        return "pokemon/game/step1";
    }

    @PostMapping("/step2")
    public String step2() {

        return "pokemon/game/step2";
    }
}
