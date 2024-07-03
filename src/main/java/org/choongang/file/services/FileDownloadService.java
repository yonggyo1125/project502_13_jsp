package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.exceptions.FileNotFoundException;
import org.choongang.global.config.annotations.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileInfoService infoService;

    /**
     * 1. 파일 정보 조회
     * 2. 파일이 있는지 체크
     * 3. 응답 헤더 : Content-Disposition ...
     * 4. 응답 바디 : 파일 데이터 출력
     * @param seq
     */
    public void download(long seq) {
        FileInfo data = infoService.get(seq).orElseThrow(FileNotFoundException::new);
        String filePath = data.getFilePath();
        File file = new File(filePath);

        if (!file.exists()) { // 파일을 찾을 수 없다면
            throw new FileNotFoundException();
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
