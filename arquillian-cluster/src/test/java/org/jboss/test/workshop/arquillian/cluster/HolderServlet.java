package org.jboss.test.workshop.arquillian.cluster;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class HolderServlet extends HttpServlet {
    @Inject protected Holder holder;

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String state = req.getParameter("state");
        if (state != null) {
            holder.setState(state);
            System.out.println("Holder::state -- set = " + state);
            resp.getWriter().write("OK");
        } else {
            state = holder.getState();
            System.out.println("Holder::state -- get = " + state);
            resp.getWriter().write(String.valueOf(state));
        }
    }
}
