package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        // webapp/static 경로 처리 S
        File file = getStaticPath(request);
        if (file.exists()) {
            Path source = file.toPath();
            String contentType = Files.probeContentType(source);
            response.setContentType(contentType);

            OutputStream out = response.getOutputStream();

            InputStream in = new BufferedInputStream(new FileInputStream(file));
            out.write(in.readAllBytes());
            return;
        }
        // webapp/static 경로 처리 E
    }

    private File getStaticPath(HttpServletRequest request) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        String path = request.getServletContext().getRealPath("/static");
        File file = new File(path + uri);

        return file;
    }
}
