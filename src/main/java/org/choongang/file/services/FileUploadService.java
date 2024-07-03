package org.choongang.file.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletDiskFileUpload;
import org.choongang.file.entities.FileInfo;
import org.choongang.global.config.annotations.Service;
import org.choongang.global.config.containers.BeanContainer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileInfoSaveService saveService;
    private final FileInfoService infoService;

    /**
     * 파일 업로드 절차
     * 1. 파일 정보 저장 - gid, location : 사용자 전달 양식 데이터
     * 2. 저장 정보 중에서 seq 번호를 가지고 파일 업로드 경로, 파일명 - 중복 방지
     *      - filePath
     * 3. 서버쪽으로 파일 저장
     * 4. 저장한 파일 정보 목록 반환
     * @return
     */
    public List<FileInfo> uploads() {
        HttpServletRequest request = BeanContainer.getInstance().getBean(HttpServletRequest.class);

        JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload();
        upload.setSizeMax(1024 * 1024 * 30); // 최대 파일 업로드 용량 30MB

        String gid = null, location = null;

        try {
            List<DiskFileItem> items = upload.parseRequest(request);

            // 양식 데이터 분리 S
            Map<String, String> formData = new HashMap<>();
            for (DiskFileItem item : items) {
                if (item.isFormField()) { // 일반 양식 데이터
                    String name = item.getFieldName();
                    String value = item.getString(Charset.forName("UTF-8"));
                    formData.put(name, value);
                }
            }

            gid = formData.getOrDefault("gid", UUID.randomUUID().toString());
            location = formData.getOrDefault("location", "");
            // 양식 데이터 분리 E

            /* 파일 업로드 처리 S */
            for (DiskFileItem item : items) {
                if (item.isFormField()) { // 일반 양식데이터는 건너뛰기
                    continue;
                }

                // 파일 정보 저장  파일명.파일명.확장자
                String fileName = item.getName();
                String extension = fileName.substring(fileName.lastIndexOf("."));
                String contentType = item.getContentType();

                FileInfo fileInfo = FileInfo.builder()
                        .gid(gid)
                        .location(location)
                        .fileName(fileName)
                        .extension(extension)
                        .contentType(contentType)
                        .build();

                fileInfo = saveService.save(fileInfo);
                if (fileInfo == null) { // 파일 정보 저장 실패 경우
                    continue;
                }

                // 파일 서버 경로에 저장
                String filePath = fileInfo.getFilePath();
                File file = new File(filePath);
                item.write(file.toPath());
            }

            /* 파일 업로드 처리 E */

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gid == null ?
                Collections.EMPTY_LIST :
                infoService.getList(gid, location);
    }
}
