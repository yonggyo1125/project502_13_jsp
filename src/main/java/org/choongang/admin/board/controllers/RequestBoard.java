package org.choongang.admin.board.controllers;

import lombok.Data;
import org.choongang.board.constants.Authority;

@Data
public class RequestBoard {
    private String mode = "register";
    private String bId; // 게시판 아이디
    private String bName; // 게시판 이름
    private int rowsPerPage = 20; // 1페이지 행 수
    private boolean active; // 사용여부
    private boolean activeCategory; // 분류 사용여부
    private String category;
    private String authority = Authority.ALL.name();
}
