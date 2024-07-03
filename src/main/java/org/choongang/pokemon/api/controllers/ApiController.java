package org.choongang.pokemon.api.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PathVariable;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.global.config.annotations.RestController;
import org.choongang.member.MemberUtil;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.exceptions.PokemonNotFoundException;
import org.choongang.pokemon.services.MyPokemonService;
import org.choongang.pokemon.services.PokemonInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pokemon")
public class ApiController {

    private final PokemonInfoService infoService;
    private final MyPokemonService pokemonService;
    private final MemberUtil memberUtil;

    @GetMapping("/random")
    public PokemonDetail random() {
        PokemonDetail data = infoService.getRandom().orElseThrow(PokemonNotFoundException::new);


        return data;
    }

    @GetMapping("/get/{seq}")
    public PokemonDetail getOne(@PathVariable("seq") long seq) {
        PokemonDetail data = infoService.get(seq).orElseThrow(PokemonNotFoundException::new);

        return data;
    }

    @GetMapping("/my/{seq}")
    public boolean myPokemon(@PathVariable("seq") long seq) {
        if (memberUtil.isLogin()) {
            return pokemonService.toggle(memberUtil.getMember().getUserNo(), seq);
        }

        return false;
    }
}
