package org.choongang.global.router;


import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.Service;

@Service
public class RouterService {
    private HttpServletRequest request;
    private HttpServletResponse response;


    /**
     * 컨트롤러 라우팅
     *
     * @param req
     * @param res
     */
    public void route(ServletRequest req, ServletResponse res) {
        request = (HttpServletRequest)req;
        response = (HttpServletResponse) res;

        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        Object controller = searchController(uri);
    }

    private Object searchController(String uri) {
        System.out.println(uri);
        return null;
    }
}
