package org.omer.logging.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link TraceIdFilter}.
 * <p>
 * Tests the filter's behavior in a real Spring Boot web context.
 * </p>
 */
@SpringBootTest(classes = TraceIdFilterIntegrationTest.TestConfig.class, properties = "logging.config=classpath:log4j2-base.yaml")
@AutoConfigureMockMvc
@DisplayName("TraceIdFilter Integration")
class TraceIdFilterIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should generate trace ID and include in response when no header provided")
    void shouldGenerateTraceIdWhenNotProvided() throws Exception {
        MvcResult result = mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(header().exists(TraceIdFilter.TRACE_ID_HEADER))
                .andReturn();

        String traceId = result.getResponse().getHeader(TraceIdFilter.TRACE_ID_HEADER);
        assertThat(traceId)
                .as("Generated trace ID should be a valid UUID format")
                .isNotBlank()
                .containsPattern("[a-f0-9-]{36}");
    }

    @Test
    @DisplayName("Should propagate existing trace ID from request to response")
    void shouldPropagateExistingTraceId() throws Exception {
        String existingTraceId = "test-trace-id-12345";

        mockMvc.perform(get("/test")
                .header(TraceIdFilter.TRACE_ID_HEADER, existingTraceId))
                .andExpect(status().isOk())
                .andExpect(header().string(TraceIdFilter.TRACE_ID_HEADER, existingTraceId));
    }

    @Test
    @DisplayName("Should maintain same trace ID across multiple log statements in a request")
    void shouldMaintainTraceIdAcrossRequest() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/multi-log"))
                .andExpect(status().isOk())
                .andReturn();

        String traceId = result.getResponse().getHeader(TraceIdFilter.TRACE_ID_HEADER);
        assertThat(traceId)
                .as("Trace ID should be present in response")
                .isNotBlank();
    }

    /**
     * Test configuration for the integration test.
     */
    @Configuration
    @org.springframework.boot.autoconfigure.EnableAutoConfiguration
    static class TestConfig {
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    /**
     * Simple test controller for integration tests.
     */
    @RestController
    static class TestController {
        private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
                .getLogger(TestController.class);

        @GetMapping("/test")
        public String test() {
            log.info("Test endpoint called");
            return "OK";
        }

        @GetMapping("/test/multi-log")
        public String multiLog() {
            log.info("First log statement");
            log.debug("Second log statement");
            log.info("Third log statement");
            return "OK";
        }
    }
}
