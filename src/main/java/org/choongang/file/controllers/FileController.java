package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.FileDownloadService;
import org.choongang.file.services.FileUploadService;
import org.choongang.global.config.annotations.PathVariable;
import org.choongang.global.config.annotations.PostMapping;
import org.choongang.global.config.annotations.RequestMapping;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.exceptions.ExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService uploadService;
    private final FileDownloadService downloadService;

    @PostMapping("/upload")
    public List<FileInfo> upload() {
        List<FileInfo> items = uploadService.uploads();

        return items;
    }

    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") long seq) {
        downloadService.download(seq);
    }

    @ExceptionHandler(Exception.class)
    public boolean errorHandler(Exception e) {
        e.printStackTrace();

        return false;
    }
 }
