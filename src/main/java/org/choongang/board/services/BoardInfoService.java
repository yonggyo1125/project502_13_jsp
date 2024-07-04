package org.choongang.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.choongang.board.controllers.BoardSearch;
import org.choongang.board.controllers.RequestBoardData;
import org.choongang.board.entities.Board;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardConfigNotFoundException;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.board.services.config.BoardConfigInfoService;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.FileInfoService;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@RequiredArgsConstructor
public class BoardInfoService {
    private final BoardDataMapper mapper;
    private final BoardConfigInfoService configInfoService;
    //private final BoardAuthService authService;
    private final FileInfoService fileInfoService;

    private Board board;

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

        addBoardData(data);

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

    /**
     * 게시글 목록
     *
     * @param search
     * @return - 조회된 목록 + 페이징
     */
    public ListData<BoardData> getList(BoardSearch search) {
        if (board == null) {
            board = configInfoService.get(search.getBId()).orElseThrow(BoardConfigNotFoundException::new);
        }

        // 권한 체크
        //authService.setBoard(board);
        //authService.check(search.getBId(), "list");

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? Math.max(board.getRowsPerPage(), 1) : limit;

        int offset = (page - 1) * limit + 1;
        int endRows = offset + limit;
        search.setOffset(offset);
        search.setEndRows(endRows);

        List<BoardData> items = mapper.getList(search);

        // 추가 데이터 처리
        items.forEach(this::addBoardData);

        // 페이징 처리
        int total = mapper.getTotal(search);
        HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);
        Pagination pagination = new Pagination(page, total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    public ListData<BoardData> getList(String bId, BoardSearch search) {
        search.setBId(bId);

        return getList(search);
    }

    public ListData<BoardData> getList(String bId) {
        return getList(bId, new BoardSearch());
    }

    /**
     * 게시글에 추가될 정보 처리
     *      - 에디터 첨부 이미지 파일, 일반 첨부 파일
     * @param data
     */
    private void addBoardData(BoardData data) {
        if (data == null) {
            return;
        }

        String gid = data.getGId();

        // 에디터 첨부 이미지 파일 목록
        List<FileInfo> editorFiles = fileInfoService.getListDone(gid, "editor");

        // 일반 첨부 파일 목록
        List<FileInfo> attachFiles = fileInfoService.getListDone(gid, "attach");

        data.setEditorFiles(editorFiles);
        data.setAttachFiles(attachFiles);
    }
}
