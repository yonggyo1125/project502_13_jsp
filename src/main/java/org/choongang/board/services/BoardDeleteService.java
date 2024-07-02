package org.choongang.board.services;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.BoardData;
import org.choongang.board.exceptions.BoardNotFoundException;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.global.config.annotations.Service;

@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardDataMapper mapper;
    private final BoardInfoService infoService;
    private final BoardAuthService authService;

    public void delete(long seq) {

        BoardData data = infoService.get(seq).orElseThrow(BoardNotFoundException::new);
        authService.setBoardData(data);
        authService.check(seq, "delete");

        mapper.delete(seq);
    }
}
