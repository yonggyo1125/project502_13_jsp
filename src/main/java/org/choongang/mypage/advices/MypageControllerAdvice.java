package org.choongang.mypage.advices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.member.MemberUtil;

import java.io.IOException;

@RequiredArgsConstructor
@ControllerAdvice("org.choongang.mypage")
public class MypageControllerAdvice implements Interceptor {

    private final MemberUtil memberUtil;

    @Override
    public boolean preHandle() {

        /* 미로그인 -> 로그인 페이지 -> 로그인 완료 -> /mypage 자동 이동 */
        if (!memberUtil.isLogin()) {
            HttpServletResponse response = BeanContainer.getInstance().getBean(HttpServletResponse.class);
            HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);

            String url = request.getContextPath() + "/member/login?redirectUrl=/mypage";

            try {
                response.sendRedirect(url);
            } catch (IOException e) {}
            return false;
        }

        return true;
    }
}
