package org.choongang.mypage.controllers;

import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.GetMapping;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

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
    public String infoPs() {

        return "commons/execute_script";
    }
}
