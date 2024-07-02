package org.choongang.pokemon.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.mappers.PokemonMapper;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPokemonService {
    private final PokemonMapper mapper;
    private final MemberUtil memberUtil;

    /**
     * 포켓몬 저장
     *
     * @param seq
     */
    public void add(long seq) {
        if (!memberUtil.isLogin()) {
            return;
        }
        Member member = memberUtil.getMember();
        try {
            mapper.registerMyPokemon(member.getUserNo(), seq);
        } catch (Exception e) {} // 이미 등록된 USER_NO + SEQ 라면 예외 발생
    }

    public void delete(long seq) {
        if (!memberUtil.isLogin()) {
            return;
        }
        Member member = memberUtil.getMember();
        mapper.deleteMyPokemon(member.getUserNo(), seq);
    }

    public void deleteAll() {
        if (!memberUtil.isLogin()) {
            return;
        }

        Member member = memberUtil.getMember();
        mapper.deleteAllMyPokemon(member.getUserNo());
    }

    public List<PokemonDetail> getList() {
        if (memberUtil.isLogin()) {
            List<PokemonDetail> items = mapper.getMyPokemons(memberUtil.getMember().getUserNo());

            return items;
        }

        return Collections.EMPTY_LIST;
    }
}
