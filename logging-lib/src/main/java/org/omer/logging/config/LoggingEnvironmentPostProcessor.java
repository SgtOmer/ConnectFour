package org.omer.logging.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Automatically sets the default logging configuration if not already set.
 * <p>
 * This processor runs early in the application startup and sets
 * {@code logging.config}
 * to {@code classpath:log4j2-base.yaml} if the property is missing.
 * </p>
 */
public class LoggingEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String LOGGING_CONFIG_PROPERTY = "logging.config";
    private static final String DEFAULT_LOGGING_CONFIG = "classpath:log4j2-base.yaml";

    /**
     * Checks if the logging configuration is set, and if not, applies the default
     * configuration.
     * <p>
     * The default configuration is added as a low-priority property source
     * ("logging-lib-default")
     * using
     * {@link org.springframework.core.env.MutablePropertySources#addLast(org.springframework.core.env.PropertySource)}.
     * This ensures that while we provide a default, any explicit configuration
     * (command line,
     * application.properties, etc.) will still take precedence if it exists.
     * </p>
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!environment.containsProperty(LOGGING_CONFIG_PROPERTY)) {
            Map<String, Object> map = new HashMap<>();
            map.put(LOGGING_CONFIG_PROPERTY, DEFAULT_LOGGING_CONFIG);
            environment.getPropertySources().addLast(new MapPropertySource("logging-lib-default", map));
        }
    }
}
