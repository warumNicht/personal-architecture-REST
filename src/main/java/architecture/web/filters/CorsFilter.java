package architecture.web.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        System.out.println(request.getRequestURI());
        System.out.println(request.getMethod());
        System.out.println(response.getHeaderNames());

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "180");

        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            Enumeration<String> headerNames = request.getHeaderNames();
            System.out.println();
            headerNames.asIterator().forEachRemaining((d)->{
                System.out.println(d + " -> " + request.getHeader(d));
            });
            System.out.println();
            if(!request.getRequestURI().contains("/users/rest-authentication")){
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-CSRF-Token");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
