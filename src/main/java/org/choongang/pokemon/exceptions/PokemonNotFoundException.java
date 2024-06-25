package org.choongang.pokemon.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.exceptions.CommonException;

public class PokemonNotFoundException extends CommonException {
    public PokemonNotFoundException() {
        super("포켓몬을 찾을 수 없습니다.", HttpServletResponse.SC_NOT_FOUND);
    }
}
