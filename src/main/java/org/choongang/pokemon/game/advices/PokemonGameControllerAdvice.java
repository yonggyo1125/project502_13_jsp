package org.choongang.pokemon.game.advices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.MemberUtil;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.services.MyPokemonService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice("org.choongang.pokemon.game")
public class PokemonGameControllerAdvice implements Interceptor {

    private final MemberUtil memberUtil;
    private final MyPokemonService pokemonService;

    @Override
    public boolean preHandle() {
        BeanContainer bc = BeanContainer.getInstance();
        HttpServletRequest request = bc.getBean(HttpServletRequest.class);
        HttpServletResponse response = bc.getBean(HttpServletResponse.class);

        if (!memberUtil.isLogin()) {
            String url = request.getContextPath() + "/member/login?redirectUrl=/pokemon/game/step1";
            try {
                response.sendRedirect(url);
            } catch (IOException e) {}

            return false;
        }

        return true;
    }

    @ModelAttribute
    public List<PokemonDetail> myPokemons() {
        return pokemonService.getList();
    }
}
