package org.choongang.board.entities;

import lombok.Data;
import org.choongang.file.entities.FileInfo;

import java.util.List;

@Data
public abstract class AbstractBoardData {
    private List<FileInfo> editorFiles; // 에디터에 첨부한 이미지파일 목록
    private List<FileInfo> attachFiles; // 일반 첨부 파일 목록
}
