package org.choongang.admin.advices;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Interceptor;
import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;
import org.choongang.member.MemberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@ControllerAdvice("org.choongang.admin")
public class AdminControllerAdvice implements Interceptor {

    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    @Override
    public boolean preHandle() {

        if (!memberUtil.isAdmin()) {
            //throw new UnAuthorizedException();
        }

        return true;
    }

    /**
     * 서브 메뉴 전체
     *
     * @return
     */
    @ModelAttribute
    public Map<String, List<String[]>> subMenusAll() {
        Map<String, List<String[]>> menus = new HashMap<>();

        /* 게시판 관리 서브 메뉴 S */
        List<String[]> boardMenus = new ArrayList<>();
        boardMenus.add(new String[] {"게시판 목록", "/admin/board"});
        boardMenus.add(new String[] {"게시판 등록", "/admin/board/register"});
        boardMenus.add(new String[] {"게시글 관리", "/admin/board/posts"});
        menus.put("board", boardMenus);
        /* 게시판 관리 서브 메뉴 E */

        return menus;
    }

    /**
     * 주 메뉴 코드 - /admin/주메뉴 코드
     * @return
     */
    @ModelAttribute
    public String menuCode() {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        Pattern pattern = Pattern.compile("^/admin/([^/]+)/?");
        Matcher matcher = pattern.matcher(uri);

        return matcher.find() ? matcher.group(1) : "";
    }

    @ModelAttribute
    public List<String[]> subMenus() {
        Map<String, List<String[]>> menus = subMenusAll();

        String code = menuCode();

        return menus.get(code);
    }
}
