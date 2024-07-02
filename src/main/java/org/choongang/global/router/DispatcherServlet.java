package org.choongang.global.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BeanContainer bc = BeanContainer.getInstance();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        bc.setLoaded(true);
        if (check(request)) {
            bc.setLoaded(false);
        }

        bc.addBean(HttpServletRequest.class.getName(), request);
        bc.addBean(HttpServletResponse.class.getName(), response);
        bc.addBean(HttpSession.class.getName(), request.getSession());

        bc.loadBeans();

        if (check(request)) {
            bc.setLoaded(true);
        }

        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
    }

    /**
     * css, js, image 파일 요청이 아닌지 체크
     *
     * @param request
     * @return
     */
    private boolean check(HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        List<String> excludeExtensions = List.of(".css", ".js", ".png", ".jpg", ".jpeg", ".gif");

        return excludeExtensions.stream().noneMatch(s -> uri.endsWith(s));
    }
}
