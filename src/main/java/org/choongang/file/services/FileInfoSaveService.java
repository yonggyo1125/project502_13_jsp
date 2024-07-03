package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.mappers.FileInfoMapper;
import org.choongang.global.config.annotations.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileInfoSaveService {

    private final FileInfoMapper mapper;
    private final FileInfoService infoService;

    public FileInfo save(FileInfo data) {
        String gid = data.getGid();
        gid = gid == null || gid.isBlank() ? UUID.randomUUID().toString() : gid;
        data.setGid(gid);

        int result = mapper.register(data);
        if (result > 0L) {
            return infoService.get(data.getSeq()).orElse(null);
        }

        return null;
    }
}
