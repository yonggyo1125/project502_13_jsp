package org.choongang.board.mappers;

import org.choongang.board.entities.Board;

import java.util.List;

public interface BoardMapper {
    int register(Board board);
    int modify(Board board);
    int delete(String bId);
    Board get(String bId);
    List<Board> getList();
    int exists(String bId);
}
