package org.omer.logging.config;

import org.omer.logging.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import jakarta.servlet.Filter;

/**
 * Spring Boot auto-configuration for the logging library.
 * <p>
 * Automatically registers the {@link TraceIdFilter} when the application
 * is a web application and the servlet filter classes are available.
 * </p>
 * <p>
 * The filter is registered with the highest precedence to ensure trace IDs
 * are available for all subsequent filters and request processing.
 * </p>
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Filter.class)
public class LoggingAutoConfiguration {

    /**
     * Registers the TraceIdFilter as a servlet filter bean.
     * <p>
     * The filter is configured with the highest precedence to ensure it runs
     * before all other filters, making the trace ID available throughout
     * the entire request lifecycle.
     * </p>
     *
     * @return the filter registration bean
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilter() {
        FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setName("traceIdFilter");
        return registrationBean;
    }
}
