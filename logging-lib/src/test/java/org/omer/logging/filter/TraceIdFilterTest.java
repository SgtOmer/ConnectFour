package org.omer.logging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TraceIdFilter}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TraceIdFilter")
class TraceIdFilterTest {
    private TraceIdFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new TraceIdFilter();
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Nested
    @DisplayName("Trace ID Generation")
    class TraceIdGeneration {
        @Test
        @DisplayName("Should generate new trace ID when header is not present")
        void shouldGenerateTraceIdWhenHeaderNotPresent() throws ServletException, IOException {
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn(null);

            filter.doFilter(request, response, filterChain);

            verify(response).setHeader(eq(TraceIdFilter.TRACE_ID_HEADER), org.mockito.ArgumentMatchers.argThat(
                    traceId -> traceId != null && !traceId.isBlank() && traceId.contains("-")));
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should generate new trace ID when header is blank")
        void shouldGenerateTraceIdWhenHeaderIsBlank() throws ServletException, IOException {
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn("   ");

            filter.doFilter(request, response, filterChain);

            verify(response).setHeader(eq(TraceIdFilter.TRACE_ID_HEADER), org.mockito.ArgumentMatchers.argThat(
                    traceId -> traceId != null && !traceId.isBlank()));
        }

        @Test
        @DisplayName("Should use existing trace ID from header")
        void shouldUseExistingTraceIdFromHeader() throws ServletException, IOException {
            String existingTraceId = "existing-trace-id-12345";
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn(existingTraceId);

            filter.doFilter(request, response, filterChain);

            verify(response).setHeader(TraceIdFilter.TRACE_ID_HEADER, existingTraceId);
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("MDC Management")
    class MdcManagement {
        @Test
        @DisplayName("Should clear MDC after request completes")
        void shouldClearMdcAfterRequest() throws ServletException, IOException {
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn("test-trace-id");

            filter.doFilter(request, response, filterChain);

            assertThat(MDC.get(TraceIdFilter.TRACE_ID_MDC_KEY))
                    .as("MDC should be cleared after request")
                    .isNull();
        }

        @Test
        @DisplayName("Should clear MDC even when exception occurs in filter chain")
        void shouldClearMdcWhenExceptionOccurs() throws ServletException, IOException {
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn("test-trace-id");
            RuntimeException exception = new RuntimeException("Test exception");
            org.mockito.Mockito.doThrow(exception).when(filterChain).doFilter(request, response);

            assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
                    .isInstanceOf(RuntimeException.class);

            assertThat(MDC.get(TraceIdFilter.TRACE_ID_MDC_KEY))
                    .as("MDC should be cleared even after exception")
                    .isNull();
        }
    }

    @Nested
    @DisplayName("Response Header")
    class ResponseHeader {
        @Test
        @DisplayName("Should add trace ID to response header")
        void shouldAddTraceIdToResponseHeader() throws ServletException, IOException {
            String traceId = "response-trace-id";
            when(request.getHeader(TraceIdFilter.TRACE_ID_HEADER)).thenReturn(traceId);

            filter.doFilter(request, response, filterChain);

            verify(response).setHeader(TraceIdFilter.TRACE_ID_HEADER, traceId);
        }
    }
}
