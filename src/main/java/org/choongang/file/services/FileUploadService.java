package org.choongang.file.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    /**
     * 파일 업로드 절차
     * 1. 파일 정보 저장
     * 2. 저장 정보 중에서 seq 번호를 가지고 파일 업로드 경로, 파일명 - 중복 방지
     *      - filePath
     * 3. 서버쪽으로 파일 저장
     * 4. 저장한 파일 정보 목록 반환
     * @return
     */
    public List<FileInfo> uploads() {
        HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);
    }
}
