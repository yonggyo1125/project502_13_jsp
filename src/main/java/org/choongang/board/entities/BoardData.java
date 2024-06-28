package org.choongang.board.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardData {
    private long num;
    private long seq;
    private String bId;
    private String gId;
    private String poster;
    private long memberSeq;
    private String guestPassword; // 비회원 비밀번호
    private String category;
    private int notice; // 공지 여부
    private String subject;
    private String content;
    private String ua; // User-Agent : 브라우저 종류 정보
    private String ip; // 글 작성자 IP 주소
    private LocalDateTime regDt; // 작성일시
    private LocalDateTime modDt; // 수정일시
    private String email; // 회원 이메일
    private String userName; // 회원명
}
