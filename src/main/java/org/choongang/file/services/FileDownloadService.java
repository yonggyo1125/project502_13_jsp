package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;

@Service
@RequiredArgsConstructor
public class FileDownloadService {

    private final FileInfoService infoService;

    public void download(long seq) {

    }
}
