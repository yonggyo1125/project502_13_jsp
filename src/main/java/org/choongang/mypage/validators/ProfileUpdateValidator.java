package org.choongang.mypage.validators;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Component;
import org.choongang.global.exceptions.AlertException;
import org.choongang.global.exceptions.UnAuthorizedException;
import org.choongang.global.validators.RequiredValidator;
import org.choongang.global.validators.Validator;
import org.choongang.member.MemberUtil;
import org.choongang.mypage.controllers.RequestProfile;

@Component
@RequiredArgsConstructor
public class ProfileUpdateValidator implements Validator<RequestProfile>, RequiredValidator {

    private final MemberUtil memberUtil;

    @Override
    public void check(RequestProfile form) {

        // 로그인 여부 체크 
        checkTrue(memberUtil.isLogin(), new UnAuthorizedException());

        String userName = form.getUserName();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        int status = HttpServletResponse.SC_BAD_REQUEST;

        checkRequired(userName, new AlertException("회원명을 입력하세요.", status));

        if (password != null && !password.isBlank()) {
            checkRequired(confirmPassword, new AlertException("비밀번호를 확인하세요.", status));

            /* 비밀번호 자리수 체크 */
            checkTrue(password.length() >= 8, new AlertException("비밀번호는 8자리 이상 입력하세요.", status));

            /* 비밀번호 및 비밀번호 확인 일치 여부 */
            checkTrue(password.equals(confirmPassword), new AlertException("비밀번호가 일치하지 않습니다.", status));
        }
    }
}
