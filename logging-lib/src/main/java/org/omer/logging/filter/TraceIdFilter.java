package org.omer.logging.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that manages trace IDs for request correlation.
 * <p>
 * This filter extracts the trace ID from the incoming request's
 * {@code X-Trace-Id} header.
 * If no header is present, a new UUID is generated. The trace ID is stored in
 * SLF4J's
 * MDC for inclusion in log messages and is also added to the
 * response header.
 * </p>
 * <p>
 * This enables distributed tracing across multiple services when the trace ID
 * is propagated
 * in inter-service calls.
 * </p>
 */
@Slf4j
public class TraceIdFilter implements Filter {

    /** HTTP header name for trace ID propagation. */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /** MDC key for the trace ID. */
    public static final String TRACE_ID_MDC_KEY = "traceId";

    /**
     * Filters incoming requests to manage trace ID lifecycle.
     * <p>
     * Extracts or generates a trace ID, stores it in MDC for logging,
     * adds it to the response header, and ensures cleanup after the request.
     * </p>
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String traceId = extractOrGenerateTraceId(httpRequest);

        try {
            MDC.put(TRACE_ID_MDC_KEY, traceId);
            httpResponse.setHeader(TRACE_ID_HEADER, traceId);

            log.debug("Processing request with traceId: {}", traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_MDC_KEY);
        }
    }

    /**
     * Extracts the trace ID from the request header or generates a new one.
     *
     * @param request the HTTP servlet request
     * @return the trace ID (from header or newly generated)
     */
    private String extractOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
            log.trace("Generated new traceId: {}", traceId);
        } else {
            log.trace("Using existing traceId from header: {}", traceId);
        }
        return traceId;
    }
}
