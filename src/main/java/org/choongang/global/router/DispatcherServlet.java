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
import org.choongang.global.exceptions.ExceptionHandlerService;

import java.io.IOException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BeanContainer bc = BeanContainer.getInstance();
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            bc.addBean(HttpServletRequest.class.getName(), request);
            bc.addBean(HttpServletResponse.class.getName(), response);
            bc.addBean(HttpSession.class.getName(), request.getSession());

            bc.loadBeans();

            RouterService service = bc.getBean(RouterService.class);
            service.route(request, response);
        } catch (Exception e) {
            if (e instanceof ServletException || e instanceof IOException) {
                throw e;
            }

            // 예외 페이지 처리
            ExceptionHandlerService service = bc.getBean(ExceptionHandlerService.class);
            service.handle(e);
        }
    }
}
