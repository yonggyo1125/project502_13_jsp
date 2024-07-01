package org.choongang.mypage.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.member.mappers.MemberMapper;
import org.choongang.mypage.controllers.RequestProfile;
import org.choongang.mypage.validators.ProfileUpdateValidator;
import org.mindrot.jbcrypt.BCrypt;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberMapper mapper;
    private final ProfileUpdateValidator validator;
    private final MemberUtil memberUtil;

    public void update(RequestProfile form) {
        validator.check(form);

        String userName = form.getUserName();
        String password = form.getPassword();

        Member member = memberUtil.getMember(); // 로그인한 회원 정보

        member.setUserName(userName);
        if (password != null && !password.isBlank()) {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            member.setPassword(hash);
        }

        // 회원 정보 수정 처리
        mapper.modify(member);

        // 세션 데이터 업데이트
        HttpSession session = BeanContainer.getInstance().getBean(HttpSession.class);
        Member _member = mapper.get(member.getEmail());
        session.setAttribute("member", _member);
    }
}
