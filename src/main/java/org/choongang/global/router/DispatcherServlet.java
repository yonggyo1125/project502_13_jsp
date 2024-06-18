package org.choongang.global.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        BeanContainer bc = BeanContainer.getInstance();
        bc.addBean(HttpServletRequest.class.getName(), request);
        bc.addBean(HttpServletResponse.class.getName(), response);

        bc.loadBeans();

        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
    }
}
