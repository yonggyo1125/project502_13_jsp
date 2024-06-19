package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.AppConfig;
import org.choongang.global.config.annotations.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StaticResourceMappingImpl implements StaticResourceMapping {

    /**
     * 정적 자원 경로인지 체크
     *
     * @param request
     * @return
     */
    @Override
    public boolean check(HttpServletRequest request) {

        // webapp/static 경로 유무 체크
        return getStaticPath(request).exists();

    }

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // webapp/static 경로 및 파일 업로드 경로 조회
        File file = getStaticPath(request);
        if (file.exists()) {
            Path source = file.toPath();
            String contentType = Files.probeContentType(source);
            response.setContentType(contentType);

            OutputStream out = response.getOutputStream();

            InputStream in = new BufferedInputStream(new FileInputStream(file));
            out.write(in.readAllBytes());
        }
    }

    // webapp/static 경로 또는 파일 객체 경로 조회 File 객체 조회
    private File getStaticPath(HttpServletRequest request) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        String path = request.getServletContext().getRealPath("/static");
        File file = new File(path + uri);

        // webapp/static 경로에 파일이 없다면 파일 업로드 경로 File 객체 조회
        if (!file.exists()) {
            String uploadPath = AppConfig.get("file.upload.path");
            String uploadUrl = AppConfig.get("file.upload.url");
            if (uploadPath != null && !uploadPath.isBlank() && uploadUrl != null && !uploadUrl.isBlank()) {
                uri = uri.replace(uploadUrl, "");
                file = new File(uploadPath + uri);
            } // endif
        }
        return file;
    }
}
