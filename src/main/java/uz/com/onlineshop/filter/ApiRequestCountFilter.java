package uz.com.onlineshop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.com.onlineshop.service.ApiRequestCountService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiRequestCountFilter implements Filter {

    private final ApiRequestCountService apiRequestCountService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            apiRequestCountService.incrementRequestCount(requestUri);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
