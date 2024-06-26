package org.choongang.member.validators;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.entities.Member;
import org.choongang.member.mappers.MemberMapper;
import org.mindrot.jbcrypt.BCrypt;

@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator<RequestLogin>, RequiredValidator {

    private final MemberMapper mapper;

    @Override
    public void check(RequestLogin form) {
        String email = form.getEmail();
        String password = form.getPassword();

        int status = HttpServletResponse.SC_BAD_REQUEST;

        // 필수 항목 검증 - 이메일, 비밀번호
        checkRequired(email, new AlertException("이메일을 입력하세요.", status));
        checkRequired(password, new AlertException("비밀번호를 입력하세요.", status));

        // 가입된 회원인지 체크
        String message = "이메일 또는 비밀번호가 일치하지 않습니다.";
        Member member = mapper.get(email);
        checkTrue(member != null, new AlertException(message, status));

        // 비밀번호 일치 여부 체크
        checkTrue(BCrypt.checkpw(password, member.getPassword()), new AlertException(message, status));
    }
}
