package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.constants.UserType;
import org.choongang.member.entities.Member;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.services.PokemonInfoService;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final PokemonInfoService infoService;

    // 로그인 여부
    public boolean isLogin() {
        return getMember() != null;
    }

    // 관리자 여부
    public boolean isAdmin() {
        if (isLogin()) {
            Member member = getMember();

            return member.getUserType() == UserType.ADMIN;
        }

        return false;
    }

    /**
     * 로그인한 회원 정보
     *
     * @return
     */
    public Member getMember() {
        HttpSession session = BeanContainer.getInstance().getBean(HttpSession.class);
        if (session == null) {
            return null;
        }

        Member member = (Member)session.getAttribute("member");

        return member;
    }

    public PokemonDetail getMyProfile() {
        if (isLogin()) {
            Member member = getMember();
            long seq = member.getMyPokemonSeq();
            if (seq > 0L) {
                return infoService.get(seq).orElse(null);
            }
        }

        return null;
    }
}
