package org.choongang.mypage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.mypage.services.ProfileService;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final ProfileService profileService;

    /**
     * 마이페이지 메인
     *
     * @return
     */
    @GetMapping
    public String index() {

        return "mypage/index";
    }

    /**
     * 회원 정보 확인 및 수정
     *
     * @return
     */
    @GetMapping("/info")
    public String info() {
        return "mypage/info";
    }

    /**
     * 회원 정보 확인 및 수정
     * @return
     */
    @PostMapping("/info")
    public String infoPs(RequestProfile form, HttpServletRequest request) {

        profileService.update(form);

        String url = request.getContextPath() + "/mypage";
        String script = String.format("script", "parent.location.replace('%s');", url);

        request.setAttribute("script", script);

        return "commons/execute_script";
    }
}
