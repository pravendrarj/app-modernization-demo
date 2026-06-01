package com.legacy.inventory.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * OUTDATED PATTERNS:
 * - Uses javax.servlet.Filter (should be jakarta.servlet.Filter)
 * - Manual request logging (should use Spring Boot Actuator or Micrometer)
 * - Uses java.util.Date
 * - Direct System.currentTimeMillis() timing
 * - No structured logging
 */
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("RequestLoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        String remoteAddr = httpRequest.getRemoteAddr();

        // OUTDATED: String concatenation logging
        logger.info("Incoming request: " + method + " " + requestURI +
                    " from " + remoteAddr + " at " + new Date());

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;
        int status = httpResponse.getStatus();

        logger.info("Completed: " + method + " " + requestURI +
                    " | Status: " + status +
                    " | Duration: " + duration + "ms");
    }

    @Override
    public void destroy() {
        logger.info("RequestLoggingFilter destroyed");
    }
}
