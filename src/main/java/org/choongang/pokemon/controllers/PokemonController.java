package org.choongang.pokemon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.config.annotations.*;
import org.choongang.global.exceptions.UnAuthorizedException;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.mypage.controllers.RequestProfile;
import org.choongang.mypage.services.ProfileService;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.exceptions.PokemonNotFoundException;
import org.choongang.pokemon.services.MyPokemonService;
import org.choongang.pokemon.services.PokemonInfoService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonInfoService infoService;
    private final ProfileService profileService;
    private final MyPokemonService pokemonService;

    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    @GetMapping
    public String index(PokemonSearch search) {
        commonProcess();

        ListData<PokemonDetail> listData = infoService.getList(search);
        List<PokemonDetail> items = listData.getItems();
        Pagination pagination = listData.getPagination();

        request.setAttribute("items", items);
        request.setAttribute("pagination", pagination);

        return "pokemon/index";
    }

    @GetMapping("/{seq}")
    public String view(@PathVariable("seq") long seq) {
        commonProcess();

        PokemonDetail data = infoService.get(seq).orElseThrow(PokemonNotFoundException::new);

        request.setAttribute("data", data);

        return "pokemon/view";
    }

    @GetMapping("/popup/{seq}")
    public String popup(@PathVariable("seq") long seq) {

        PokemonDetail data = infoService.get(seq).orElseThrow(PokemonNotFoundException::new);

        pokemonService.add(seq); // 발급 받은 포켓몬 저장

        request.setAttribute("data", data);

        return "pokemon/popup";
    }

    @PostMapping("/popup")
    public String popupPs(@RequestParam("mode") String mode, @RequestParam("seq") long seq) {
        if (!memberUtil.isLogin()) {
            throw new UnAuthorizedException();
        }

        mode = Objects.requireNonNullElse(mode, "update");
        if (mode.equals("delete")) { // 개별 삭제
            pokemonService.delete(seq);
        } else if (mode.equals("delete-all")) { // 전체 비우기
            pokemonService.deleteAll();
        } else { // 프로필 변경
            Member member = memberUtil.getMember();
            RequestProfile form = new RequestProfile();
            form.setMyPokemonSeq(seq);
            form.setUserName(member.getUserName());
            profileService.update(form);
        }
        String script = "parent.parent.location.reload();";
        request.setAttribute("script", script);

        return "commons/execute_script";
    }

    private void commonProcess() {
        request.setAttribute("addCss", new String[] {"pokemon/style"});
        request.setAttribute("addScript", List.of("pokemon/my"));
    }
}
