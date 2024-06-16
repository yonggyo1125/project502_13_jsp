package org.choongang.global.router;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;

@WebServlet(urlPatterns = "/", initParams = @WebInitParam(name="packageName", value="org.choongang"))
public class DispatcherServlet extends HttpServlet  {
    @Override
    public void init(ServletConfig config) throws ServletException {
        String packageName = config.getInitParameter("packageName");
        BeanContainer.getInstance().loadBeans(packageName);



    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {


    }
}
