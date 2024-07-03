package org.choongang.file.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileInfo extends AbstractFileInfo {
    private long seq;
    private String gid;
    private String location;
    private String fileName; // 파일명
    private String extension; // 파일 확장자
    private String contentType; // 파일 형식
    private int done; // 그룹 작업 완료 여부
    private LocalDateTime regDt;
}
