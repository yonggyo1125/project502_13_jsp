package org.choongang.board.controllers;

import lombok.Data;

@Data
public class BoardSearch {
    private int page = 1;
    private int limit = 20;
    private int offset;
    private int endRows;
    private String bId; // 게시판 ID
    private String sopt;
    private String skey;
}
