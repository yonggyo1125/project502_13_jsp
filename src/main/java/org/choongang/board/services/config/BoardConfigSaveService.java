package org.choongang.board.services.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.RequestBoard;
import org.choongang.board.constants.Authority;
import org.choongang.board.entities.Board;
import org.choongang.board.mappers.BoardMapper;
import org.choongang.board.validators.config.BoardConfigValidator;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.exceptions.AlertException;

@Service
@RequiredArgsConstructor
public class BoardConfigSaveService {
    private final BoardMapper mapper;
    private final BoardConfigValidator validator;

    public void process(RequestBoard form) {
        // 유효성 검사
        validator.check(form);

        Board board = Board.builder()
                .bId(form.getBId())
                .bName(form.getBName())
                .active(form.isActive()?1:0)
                .activeCategory(form.isActiveCategory()?1:0)
                .category(form.getCategory())
                .rowsPerPage(form.getRowsPerPage())
                .authority(Authority.valueOf(form.getAuthority()))
                .build();

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "register" : mode;

        int result = 0;
        if (mode.equals("update")) { // 수정
            result = mapper.modify(board);
        } else { // 추가
            result = mapper.register(board);
        }

        if (result < 1) {
            throw new AlertException("게시판 " + (mode.equals("update") ? "수정":"등록") + "에 실패하였습니다.", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
