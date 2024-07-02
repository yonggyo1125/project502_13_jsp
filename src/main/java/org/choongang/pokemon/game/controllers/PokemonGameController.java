package org.choongang.pokemon.game.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pokemon/game")
@RequiredArgsConstructor
public class PokemonGameController {

    private final HttpServletRequest request;

    @GetMapping
    public String index() {

        return "redirect:/pokemon/game/step1";
    }

    /**
     * 1단계 : 카드 선택
     * 2단계 : 게임 시작, 결과
     *
     * @return
     */
    @GetMapping("/step1")
    public String step1() {
        commonProcess();

        return "pokemon/game/step1";
    }

    @PostMapping("/step2")
    public String step2() {
        commonProcess();

        return "pokemon/game/step2";
    }

    private void commonProcess() {
        request.setAttribute("addCss", List.of("pokemon/game"));
    }
}
