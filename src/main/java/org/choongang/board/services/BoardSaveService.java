package org.choongang.board.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.entities.BoardData;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.board.validators.BoardSaveValidator;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;
import org.choongang.global.exceptions.AlertException;
import org.choongang.member.MemberUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import java.util.Objects;
import java.util.Optional;

/**
 * 게시글 추가, 수정
 *
 */
@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardDataMapper mapper;
    private final BoardSaveValidator validator;
    private final MemberUtil memberUtil;
    private final BoardInfoService infoService;

    public Optional<BoardData> save(RequestBoardData form) {

        validator.check(form);

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "write" : mode;

        BoardData data = new ModelMapper().map(form, BoardData.class);

        if (mode.equals("write")) { // 등록
            // 현재 로그인한 회원 번호
            if (memberUtil.isLogin()) {
                data.setMemberSeq(memberUtil.getMember().getUserNo());
            }

            // 작성자 환경 정보
            HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);
            if (request != null) {
                String ua = request.getHeader("User-Agent"); // 작성자 브라우저 정보
                String ip = request.getRemoteAddr(); // 작성자 IP 정보
                data.setUa(ua);
                data.setIp(ip);
            } else {
                data.setUa("");
                data.setIp("");
            }
        }

        // 비회원 비밀번호 해시화
        if (!memberUtil.isLogin()) {
            String hash = BCrypt.hashpw(form.getGuestPassword(), BCrypt.gensalt(12));
            data.setGuestPassword(hash);
        } else {
            data.setGuestPassword("");
        }

        String category = data.getCategory();
        data.setCategory(Objects.requireNonNullElse(category, ""));

        if (mode.equals("update")) {
            mapper.modify(data);
        } else {
            int result = mapper.register(data);
            if (result < 1) {
                throw new AlertException("게시글 등록에 실패하였습니다.", HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        return infoService.get(data.getSeq());
    }
}
