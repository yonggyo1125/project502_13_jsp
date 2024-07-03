package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.mappers.FileInfoMapper;
import org.choongang.global.config.annotations.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileInfoService {
    private final FileInfoMapper mapper;

    public Optional<FileInfo> get(long seq) {

        FileInfo data = mapper.get(seq);

        /**
         * 실제 파일 업로드 path 예) d:/uploads/0/100.png ...
         * 실제 파일 접근 URL  예) /uploads/0/100.png ...
         */
        addFileInfo(data);

        return Optional.ofNullable(data);
    }

    /**
     *
     * @param gid
     * @param location
     * @param mode : ALL - 전체 파일, DONE - 그룹작업 완료 파일, UNDONE - 그룹작업 미완료 파일
     * @return
     */
    public List<FileInfo> getList(String gid, String location, String mode) {
        mode = Objects.requireNonNullElse(mode, "ALL");
        if (mode.equals("DONE")) {
            return mapper.getListDone(gid, location);

        } else if (mode.equals("UNDONE")) {
            return mapper.getListUnDone(gid, location);

        } else {
            return mapper.getList(gid, location);
        }
    }

    public List<FileInfo> getList(String gid, String location) {
        return getList(gid, location, "ALL");
    }

    public List<FileInfo> getList(String gid) {
        return getList(gid, null);
    }

    public List<FileInfo> getListDone(String gid, String location) {
        return getList(gid, location, "DONE");
    }

    public List<FileInfo> getListDone(String gid) {
        return getListDone(gid, null);
    }

    private void addFileInfo(FileInfo data) {

    }
}
