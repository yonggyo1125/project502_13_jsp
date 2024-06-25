package org.choongang.member.validators;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.validators.EmailValidator;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.mappers.MemberMapper;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator<RequestJoin>, RequiredValidator, EmailValidator {

    private final MemberMapper mapper;

    @Override
    public void check(RequestJoin form) {
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String userName = form.getUserName();
        boolean termsAgree = form.isTermsAgree();
        int status = HttpServletResponse.SC_BAD_REQUEST;

        /* 필수 항목 유효성 검사 S */
        checkRequired(email, new AlertException("이메일을 입력하세요", status));
        checkRequired(password, new AlertException("비밀번호를 입력하세요.", status));
        checkRequired(confirmPassword, new AlertException("비밀번호를 확인하세요.", status));
        checkRequired(userName, new AlertException("회원명을 입력하세요.", status));
        checkTrue(termsAgree, new AlertException("약관에 동의해 주세요.", status));
        /* 필수 항목 유효성 검사 E */
    }
}
