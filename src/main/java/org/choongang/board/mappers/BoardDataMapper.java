package org.choongang.board.mappers;

import org.choongang.board.controllers.BoardSearch;
import org.choongang.board.entities.BoardData;

import java.util.List;

public interface BoardDataMapper {
    int register(BoardData data);
    int modify(BoardData data);
    int delete(long seq);
    BoardData get(long seq);
    List<BoardData> getList(BoardSearch search);
}
