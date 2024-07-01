package org.choongang.board.services;

import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.entities.BoardData;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.board.validators.BoardSaveValidator;
import org.choongang.global.config.DBConn;
import org.choongang.member.MemberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@DisplayName("게시글 등록, 수정 테스트")
public class BoardSaveServiceTest {
    private BoardSaveService saveService;

    @Mock
    private MemberUtil memberUtil;

    @BeforeEach
    void init() {
        given(memberUtil.isLogin()).willReturn(false);

        BoardDataMapper mapper = DBConn.getSession().getMapper(BoardDataMapper.class);
        BoardInfoService infoService = new BoardInfoService(mapper);
        BoardSaveValidator validator = new BoardSaveValidator(memberUtil, mapper);
        saveService = new BoardSaveService(mapper, validator, memberUtil, infoService);
    }

    @Test
    void saveTest() {
        RequestBoardData form = new RequestBoardData();
        form.setBId("freetalk");
        form.setCategory("분류1");
        form.setPoster("작성자");
        form.setSubject("제목");
        form.setContent("내용");
        form.setGuestPassword("1234");
        form.setMode("write");

        Optional<BoardData> data = saveService.save(form);

        BoardData bData = data.orElse(null);
        assertNotNull(bData);

        System.out.println(bData);
    }
}
