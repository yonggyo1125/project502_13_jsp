package org.choongang.mypage.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
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

        Member member = memberUtil.getMember();
        member.setUserName(userName);
        if (password != null && !password.isBlank()) {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        }
    }
}
