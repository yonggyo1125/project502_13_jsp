package org.choongang.board.controllers;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestBoardData {
    private String mode;
    private long seq;
    private String bId;
    private String gId = UUID.randomUUID().toString();
    private String category;
    private String poster;
    private boolean notice; // 공지사항 여부
    private String guestPassword; // 비회원 비밀번호
    private String subject;
    private String content;
}
