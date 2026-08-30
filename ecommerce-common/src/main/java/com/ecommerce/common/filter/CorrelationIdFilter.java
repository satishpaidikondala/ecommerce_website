package com.ecommerce.common.filter;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class CorrelationIdFilter implements Filter {

    public static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        String id = httpReq.getHeader(CORRELATION_ID);
        if (id == null) id = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID, id);
        httpRes.setHeader(CORRELATION_ID, id);
        try { chain.doFilter(req, res); } finally { MDC.remove(CORRELATION_ID); }
    }
}
