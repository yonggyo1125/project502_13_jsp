package org.choongang.pokemon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {
    private final HttpServletRequest request;

    @GetMapping
    public String index() {
        String uri = request.getRequestURI();
        System.out.println(uri);
        return "pokemon/index";
    }
}
