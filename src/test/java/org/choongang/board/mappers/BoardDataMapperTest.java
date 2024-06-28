package org.choongang.board.mappers;

import org.apache.ibatis.session.SqlSession;
import org.choongang.board.constants.Authority;
import org.choongang.board.controllers.BoardSearch;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.global.config.DBConn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BoardDataMapperTest {

    private SqlSession session;
    private BoardMapper mapper;
    private BoardDataMapper dataMapper;

    @BeforeEach
    void init() {
        session = DBConn.getSession(false);

        mapper = session.getMapper(BoardMapper.class);
        dataMapper = session.getMapper(BoardDataMapper.class);
    }

    @Test
    void registerTest() {
        Board board = Board.builder()
                .bId("B" + System.currentTimeMillis())
                .bName("테스트 게시판")
                .rowsPerPage(20)
                .authority(Authority.ALL)
                .active(1)
                .activeCategory(0)
                .category("")
                .build();
        mapper.register(board);


        BoardData data = BoardData.builder()
                .bId(board.getBId())
                .gId(UUID.randomUUID().toString())
                .poster("작성자")
                .subject("제목")
                .content("내용")
                .guestPassword("")
                .category("")
                .ua("")
                .ip("")
                .build();

        int result = dataMapper.register(data);

        BoardData data2 = dataMapper.get(data.getSeq());

        assertNotNull(data2);

        assertEquals(data.getSeq(), data2.getSeq());

        System.out.println(data2);

        int result2 = dataMapper.delete(data.getSeq());
        System.out.println(result2);

        BoardData data3 = dataMapper.get(data.getSeq());
        assertNull(data3);
    }


    @Test
    void getListTest() {
        Board board = Board.builder()
                .bId("B" + System.currentTimeMillis())
                .bName("테스트 게시판")
                .rowsPerPage(20)
                .authority(Authority.ALL)
                .active(1)
                .activeCategory(0)
                .category("")
                .build();
        mapper.register(board);

        for(int i = 0; i < 10; i++) {
            BoardData data = BoardData.builder()
                    .bId(board.getBId())
                    .gId(UUID.randomUUID().toString())
                    .poster("작성자")
                    .subject("제목" + i)
                    .content("내용" + i)
                    .guestPassword("")
                    .category("")
                    .ua("")
                    .ip("")
                    .build();

            dataMapper.register(data);
        }

        BoardSearch search = new BoardSearch();
        int page = 1;
        int limit = 10;
        int offset = (page - 1) * limit + 1;
        int endRows = offset + limit;

        search.setPage(page);
        search.setLimit(limit);
        search.setOffset(offset);
        search.setEndRows(endRows);
        search.setBId(board.getBId());
        search.setSopt("SUBJECT_CONTENT");
        search.setSkey("제목");

        List<BoardData> items = dataMapper.getList(search);
        assertNotNull(items);
       // assertTrue(items.size() == 10);

        items.forEach(System.out::println);

    }


    @AfterEach
    void destroy() {

        session.rollback();
    }
}
