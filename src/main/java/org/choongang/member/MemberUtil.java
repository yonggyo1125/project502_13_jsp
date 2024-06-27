package org.choongang.member;

import jakarta.servlet.http.HttpSession;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.constants.UserType;
import org.choongang.member.entities.Member;

@Component
public class MemberUtil {
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
        Member member = (Member)session.getAttribute("member");

        return member;
    }
}
