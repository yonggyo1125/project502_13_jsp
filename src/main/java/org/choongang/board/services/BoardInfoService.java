package org.choongang.board.services;

import lombok.RequiredArgsConstructor;
import org.choongang.board.controllers.BoardSearch;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.global.ListData;
import org.choongang.global.config.annotations.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardInfoService {
    private final BoardDataMapper mapper;

    /**
     * 게시글 번호로 게시글 조회
     *      - 쓰기, 수정, 목록, 보기 권한 데이터 추가
     *      - 댓글 목록 데이터 추가
     *      - 게시글에 첨부한 이미지 또는 파일 목록
     *
     * @param seq
     * @return
     */
    public Optional<BoardData> get(long seq) {
        BoardData data = mapper.get(seq);


        return Optional.ofNullable(data);
    }


    public RequestBoardData getForm(long seq) {
        BoardData data = get(seq).orElseThrow(BoardNotFoundException::new);

        return getForm(data);
    }

    public RequestBoardData getForm(BoardData data) {
        RequestBoardData form = new ModelMapper().map(data, RequestBoardData.class);
        form.setMode("update");

        return form;
    }

    public ListData<BoardData> getList(BoardSearch search) {

        return null;
    }

    public ListData<BoardData> getList(String bId, BoardSearch search) {
        search.setBId(bId);

        return getList(search);
    }

}
