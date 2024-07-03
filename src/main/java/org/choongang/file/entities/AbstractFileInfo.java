package org.choongang.file.entities;

import lombok.Data;

@Data
public abstract class AbstractFileInfo {
    private String filePath; // 파일 업로드 서버 경로
    private String fileUrl; // 웹 접근 주소
}
